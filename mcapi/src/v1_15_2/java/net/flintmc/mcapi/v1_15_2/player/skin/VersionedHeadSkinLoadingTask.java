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

package net.flintmc.mcapi.v1_15_2.player.skin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.mcapi.internal.resources.MinecraftSkinCacheDirectoryProvider;
import net.flintmc.mcapi.player.gameprofile.property.Property;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.ResourceLocationWatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.SkinManager.ISkinAvailableCallback;
import net.minecraft.entity.player.PlayerEntity;

public class VersionedHeadSkinLoadingTask implements ISkinAvailableCallback,
    ResourceLocationWatcher {

  private final MinecraftSkinCacheDirectoryProvider minecraftSkinCacheDirectoryProvider;
  private final ResourceLocation.Factory resourceLocationFactory;
  private final GameProfile gameProfile;
  private final Collection<Consumer<ResourceLocation>> loadCallbacks;
  private ResourceLocation currentLocation;
  private boolean loaded = false;

  @AssistedInject
  private VersionedHeadSkinLoadingTask(
      MinecraftSkinCacheDirectoryProvider minecraftSkinCacheDirectoryProvider,
      ResourceLocation.Factory resourceLocationFactory,
      @Assisted net.flintmc.mcapi.player.gameprofile.GameProfile flintProfile) {
    this.minecraftSkinCacheDirectoryProvider = minecraftSkinCacheDirectoryProvider;
    this.resourceLocationFactory = resourceLocationFactory;
    this.gameProfile = convertProfile(flintProfile);
    this.loadCallbacks = new HashSet<>();
  }

  protected void load() {
    SkinManager skinManager = Minecraft.getInstance().getSkinManager();
    Map<Type, MinecraftProfileTexture> skinMap = skinManager
        .loadSkinFromCache(this.gameProfile);

    if (skinMap.containsKey(Type.SKIN)) {
      net.minecraft.util.ResourceLocation resourceLocation = Minecraft.getInstance()
          .getSkinManager().loadSkin(skinMap.get(Type.SKIN), Type.SKIN, this);
      this.currentLocation = convertResourceLocation(resourceLocation);
      this.createHeadLocation();
    } else {
      net.minecraft.util.ResourceLocation resourceLocation = DefaultPlayerSkin
          .getDefaultSkin(PlayerEntity.getUUID(this.gameProfile));
      this.currentLocation = convertResourceLocation(resourceLocation);
      this.createHeadLocation();
      skinManager.loadProfileTextures(gameProfile, this, false);
    }
  }

  private GameProfile convertProfile(
      net.flintmc.mcapi.player.gameprofile.GameProfile flintProfile) {
    GameProfile mojangProfile = new GameProfile(
        flintProfile.getUniqueId(), flintProfile.getName());

    for (Entry<String, Property> entry : flintProfile.getProperties().entries()) {
      mojangProfile.getProperties().put(entry.getKey(),
          new com.mojang.authlib.properties.Property(entry.getValue().getName(),
              entry.getValue().getValue(), entry.getValue().getSignature()));
    }

    return mojangProfile;
  }

  private ResourceLocation convertResourceLocation(
      net.minecraft.util.ResourceLocation resourceLocation) {
    return resourceLocationFactory
        .create(resourceLocation.getNamespace(), resourceLocation.getPath());
  }

  @Override
  public synchronized void onSkinTextureAvailable(Type type,
      net.minecraft.util.ResourceLocation resourceLocation,
      MinecraftProfileTexture p_onSkinTextureAvailable_3_) {
    if (type == Type.SKIN) {
      this.loaded = true;
      this.currentLocation = convertResourceLocation(resourceLocation);
      this.createHeadLocation();
      for (Consumer<ResourceLocation> loadCallback : this.loadCallbacks) {
        loadCallback.accept(this.currentLocation);
      }
      this.loadCallbacks.clear();
    }
  }

  private void createHeadLocation() {
    File headFile = new File(this.minecraftSkinCacheDirectoryProvider.getSkinCacheDirectory(),
        "heads/" + this.currentLocation.getPath());
    System.out.println();
  }

  @Override
  public synchronized ResourceLocation getCurrentLocation() {
    return this.currentLocation;
  }

  @Override
  public synchronized void registerLoadCallback(
      Consumer<ResourceLocation> loadCallback) {
    if (this.loaded) {
      loadCallback.accept(this.currentLocation);
      return;
    }
    this.loadCallbacks.add(loadCallback);
  }

  @Override
  public synchronized boolean isLoaded() {
    return this.loaded;
  }

  @AssistedFactory(VersionedHeadSkinLoadingTask.class)
  public interface Factory {

    VersionedHeadSkinLoadingTask create(
        @Assisted net.flintmc.mcapi.player.gameprofile.GameProfile gameProfilea);
  }
}
