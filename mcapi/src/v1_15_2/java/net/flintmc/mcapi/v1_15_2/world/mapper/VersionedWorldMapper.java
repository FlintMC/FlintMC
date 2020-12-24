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
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.world.mapper.WorldMapper;
import net.flintmc.mcapi.world.storage.WorldConfiguration;
import net.flintmc.mcapi.world.storage.WorldOverview;
import net.flintmc.mcapi.world.type.WorldType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSummary;

@Singleton
@Implement(value = WorldMapper.class, version = "1.15.2")
public class VersionedWorldMapper implements WorldMapper {

  private final WorldConfiguration.Factory worldConfigurationFactory;
  private final WorldType.Factory worldTypeFactory;
  private final WorldOverview.Factory worldOverviewFactory;

  @Inject
  private VersionedWorldMapper(
      WorldConfiguration.Factory worldConfigurationFactory,
      WorldType.Factory worldTypeFactory,
      WorldOverview.Factory worldOverviewFactory) {
    this.worldConfigurationFactory = worldConfigurationFactory;
    this.worldTypeFactory = worldTypeFactory;
    this.worldOverviewFactory = worldOverviewFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftWorldSettings(WorldConfiguration configuration) {
    return new WorldSettings(
        configuration.getSeed(),
        GameType.getByName(configuration.getGameMode().getName()),
        configuration.isMapFeaturesEnabled(),
        configuration.isHardcoreMode(),
        (net.minecraft.world.WorldType) this.toMinecraftWorldType(configuration.getWorldType())
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldConfiguration fromMinecraftWorldSettings(Object handle) {

    if (!(handle instanceof WorldSettings)) {
      throw new IllegalStateException(
          handle.getClass().getName()
              + " is not an instance of "
              + WorldSettings.class.getName());
    }

    WorldSettings worldSettings = (WorldSettings) handle;

    return this.worldConfigurationFactory.create(
        worldSettings.getSeed(),
        GameMode.valueOf(worldSettings.getGameType().name()),
        worldSettings.isMapFeaturesEnabled(),
        worldSettings.getHardcoreEnabled(),
        this.fromMinecraftWorldType(worldSettings.getGameType()),
        worldSettings.isBonusChestEnabled(),
        worldSettings.areCommandsAllowed(),
        worldSettings.getGeneratorOptions()
    );
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
      throw new IllegalStateException(
          handle.getClass().getName()
              + " is not an instance of "
              + net.minecraft.world.WorldType.class.getName());
    }

    net.minecraft.world.WorldType worldType = (net.minecraft.world.WorldType) handle;

    return this.worldTypeFactory.create(
        worldType.getId(),
        worldType.getName(),
        worldType.getSerialization(),
        worldType.getVersion(),
        worldType.canBeCreated(),
        worldType.isVersioned(),
        worldType.hasInfoNotice(),
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
          worldOverview.getSizeOnDisk(),
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

    WorldSummary worldSummary = (WorldSummary) handle;

    return this.worldOverviewFactory.create(
        worldSummary.getFileName(),
        worldSummary.getDisplayName(),
        worldSummary.getLastTimePlayed(),
        worldSummary.getSizeOnDisk(),
        worldSummary.requiresConversion(),
        GameMode.valueOf(worldSummary.getEnumGameType().name()),
        worldSummary.isHardcoreModeEnabled(),
        worldSummary.getCheatsEnabled(),
        worldSummary.askToOpenWorld(),
        worldSummary.markVersionInList(),
        worldSummary.func_202842_n(),
        worldSummary.func_197731_n()
    );
  }
}
