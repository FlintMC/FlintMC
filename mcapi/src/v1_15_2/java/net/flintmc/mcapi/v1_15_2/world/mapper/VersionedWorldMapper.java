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

package net.flintmc.mcapi.v1_15_2.world.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.TranslationComponent;
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.world.mapper.WorldMapper;
import net.flintmc.mcapi.world.storage.WorldOverview;
import net.flintmc.mcapi.world.type.WorldType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSummary;

@Singleton
@Implement(WorldMapper.class)
public class VersionedWorldMapper implements WorldMapper {

  private final TranslationComponent.Factory componentFactory;
  private final WorldType.Factory worldTypeFactory;
  private final WorldOverview.Factory worldOverviewFactory;

  @Inject
  private VersionedWorldMapper(
      TranslationComponent.Factory componentFactory,
      WorldType.Factory worldTypeFactory,
      WorldOverview.Factory worldOverviewFactory) {
    this.componentFactory = componentFactory;
    this.worldTypeFactory = worldTypeFactory;
    this.worldOverviewFactory = worldOverviewFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftWorldType(WorldType worldType) {
    return net.minecraft.world.WorldType.byName(worldType.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldType fromMinecraftWorldType(Object handle) {
    if (!(handle instanceof net.minecraft.world.WorldType)) {
      throw new IllegalArgumentException(
          handle.getClass().getName()
              + " is not an instance of "
              + net.minecraft.world.WorldType.class.getName());
    }

    net.minecraft.world.WorldType worldType = (net.minecraft.world.WorldType) handle;

    TranslationComponent displayName = this.componentFactory.create();
    displayName.translationKey(worldType.getTranslationKey());

    return this.worldTypeFactory.create(
        worldType.getName(),
        displayName,
        worldType.canBeCreated() && !worldType.getName()
            .equals(net.minecraft.world.WorldType.DEBUG_ALL_BLOCK_STATES.getName()),
        worldType.getName().equals(net.minecraft.world.WorldType.DEBUG_ALL_BLOCK_STATES.getName()),
        worldType.hasCustomOptions()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftWorldSummary(WorldOverview worldOverview) {
    WorldInfo worldInfo = Minecraft.getInstance().getSaveLoader()
        .getWorldInfo(worldOverview.getFileName());

    if (worldInfo != null) {
      return new WorldSummary(
          worldInfo,
          worldOverview.getFileName(),
          worldOverview.getDisplayName(),
          0L,
          worldOverview.requiresConversion()
      );
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldOverview fromMinecraftWorldSummary(Object handle) {
    if (!(handle instanceof WorldSummary)) {
      throw new IllegalStateException(
          handle.getClass().getName()
              + " is not an instance of "
              + WorldInfo.class.getName());
    }

    WorldSummary summary = (WorldSummary) handle;
    String rawVersion = ((WorldSummaryShadow) summary).getRawVersionName();

    return this.worldOverviewFactory.create(
        summary.getFileName(),
        summary.getDisplayName(),
        rawVersion == null || rawVersion.isEmpty() ? null : rawVersion,
        summary.getLastTimePlayed(),
        summary.requiresConversion(),
        GameMode.valueOf(summary.getEnumGameType().name()),
        summary.isHardcoreModeEnabled(),
        summary.getCheatsEnabled(),
        summary.askToOpenWorld(),
        summary.markVersionInList(),
        summary.func_202842_n(),
        summary.func_197731_n()
    );
  }
}
