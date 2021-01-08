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

package net.flintmc.mcapi.v1_16_4.world.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.world.mapper.WorldMapper;
import net.flintmc.mcapi.world.storage.WorldOverview;
import net.flintmc.mcapi.world.type.WorldType;
import net.minecraft.server.SessionLockManager;
import net.minecraft.util.SharedConstants;
import net.minecraft.world.storage.VersionData;
import net.minecraft.world.storage.WorldSummary;

import java.io.File;
import java.io.IOException;

@Singleton
@Implement(value = WorldMapper.class, version = "1.16.4")
public class VersionedWorldMapper implements WorldMapper {

  private final WorldType.Factory worldTypeFactory;
  private final WorldOverview.Factory worldOverviewFactory;

  @Inject
  private VersionedWorldMapper(
      WorldType.Factory worldTypeFactory,
      WorldOverview.Factory worldOverviewFactory) {
    this.worldTypeFactory = worldTypeFactory;
    this.worldOverviewFactory = worldOverviewFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftWorldType(WorldType worldType) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldType fromMinecraftWorldType(Object handle) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftWorldSummary(WorldOverview worldOverview) {
    File file = new File(worldOverview.getFileName());

    try {
      // TODO: 30.12.2020 We need a better api for this.
      return new WorldSummary(
          null,
          new VersionData(0, 0, SharedConstants.getVersion().getName(),
              SharedConstants.getVersion().getWorldVersion(),
              !SharedConstants.getVersion().isStable()),
          worldOverview.getFileName(),
          worldOverview.requiresConversion(),
          SessionLockManager.func_232999_b_(file.toPath()),
          new File(worldOverview.getFileName() + "/icon.png"));
    } catch (IOException exception) {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldOverview fromMinecraftWorldSummary(Object handle) {
    if (!(handle instanceof WorldSummary)) {
      return null;
    }

    WorldSummary worldSummary = (WorldSummary) handle;

    // TODO: 29.12.2020 Disk size
    return this.worldOverviewFactory.create(
        worldSummary.getFileName(),
        worldSummary.getDisplayName(),
        worldSummary.getVersionName().getString(),
        worldSummary.getLastTimePlayed(),
        0L, /* Disk size is maybe removed since 1.16.x*/
        worldSummary.requiresConversion(),
        GameMode.valueOf(worldSummary.getEnumGameType().name()),
        worldSummary.isHardcoreModeEnabled(),
        worldSummary.getCheatsEnabled(),
        worldSummary.askToOpenWorld(),
        worldSummary.markVersionInList(),
        worldSummary.askToOpenWorld(),
        worldSummary.askToCreateBackup());
  }
}
