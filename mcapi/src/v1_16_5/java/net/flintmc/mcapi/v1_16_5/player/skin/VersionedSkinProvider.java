/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.v1_16_5.player.skin;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.skin.SkinProvider;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.ResourceLocationWatcher;
import net.flintmc.mcapi.v1_16_5.player.skin.VersionedSkinLoadingTask.Factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Singleton
@Implement(SkinProvider.class)
public class VersionedSkinProvider implements SkinProvider {

  private final VersionedSkinLoadingTask.Factory versionedSkinLoadingTaskFactory;
  private final VersionedHeadSkinLoadingTask.Factory versionedHeadSkinLoadingTaskFactory;
  private final Map<GameProfile, ResourceLocationWatcher> watchers = new ConcurrentHashMap<>();
  private final ResourceLocation.Factory resourceLocationFactory;

  @Inject
  private VersionedSkinProvider(
      Factory versionedSkinLoadingTaskFactory,
      VersionedHeadSkinLoadingTask.Factory versionedHeadSkinLoadingTaskFactory,
      ResourceLocation.Factory resourceLocationFactory) {
    this.versionedSkinLoadingTaskFactory = versionedSkinLoadingTaskFactory;
    this.versionedHeadSkinLoadingTaskFactory = versionedHeadSkinLoadingTaskFactory;
    this.resourceLocationFactory = resourceLocationFactory;
  }

  @Override
  public ResourceLocationWatcher loadSkin(GameProfile gameProfile) {
    return this.loadSkin(gameProfile, resourceLocation -> {
    });
  }

  @Override
  public ResourceLocationWatcher loadHeadSkin(GameProfile gameProfile) {
    return this.loadHeadSkin(gameProfile, resourceLocation -> {});
  }

  @Override
  public ResourceLocationWatcher loadSkin(GameProfile gameProfile,
      Consumer<ResourceLocation> updateCallback) {
    ResourceLocationWatcher resourceLocationWatcher = this.watchers
        .computeIfAbsent(gameProfile, key -> {
          VersionedSkinLoadingTask versionedSkinLoadingTask = versionedSkinLoadingTaskFactory
              .create(gameProfile);
          versionedSkinLoadingTask.registerLoadCallback(updateCallback);
          versionedSkinLoadingTask.registerLoadCallback(resourceLocation -> {
            this.watchers.remove(gameProfile);
          });
          versionedSkinLoadingTask.load();
          return versionedSkinLoadingTask;
        });
    if(resourceLocationWatcher.isLoaded()){
      this.watchers.remove(gameProfile);
    }
    return resourceLocationWatcher;
  }

  @Override
  public ResourceLocationWatcher loadHeadSkin(GameProfile gameProfile,
      Consumer<ResourceLocation> updateCallback) {
    ResourceLocationWatcher resourceLocationWatcher = this.watchers
        .computeIfAbsent(gameProfile, key -> {
          VersionedHeadSkinLoadingTask versionedHeadSkinLoadingTask = versionedHeadSkinLoadingTaskFactory
              .create(gameProfile);
          versionedHeadSkinLoadingTask.registerLoadCallback(updateCallback);
          versionedHeadSkinLoadingTask.registerLoadCallback(resourceLocation -> {
            this.watchers.remove(gameProfile);
          });
          versionedHeadSkinLoadingTask.load();
          return versionedHeadSkinLoadingTask;
        });
    if(resourceLocationWatcher.isLoaded()){
      this.watchers.remove(gameProfile);
    }
    return resourceLocationWatcher;
  }
}
