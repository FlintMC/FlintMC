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

package net.flintmc.mcapi.v1_16_4.world.storage;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.mapper.WorldMapper;
import net.flintmc.mcapi.world.storage.WorldOverview;
import net.flintmc.mcapi.world.storage.service.WorldLoader;
import net.flintmc.mcapi.world.storage.service.exception.WorldLoadException;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.world.storage.SaveFormat;
import net.minecraft.world.storage.WorldSummary;

@Singleton
@Implement(value = WorldLoader.class, version = "1.16.4")
public class VersionedWorldLoader implements WorldLoader {

  private final WorldMapper worldMapper;
  private final Map<String, WorldOverview> worldOverviews;
  private boolean loaded;

  @Inject
  private VersionedWorldLoader(WorldMapper worldMapper) {
    this.worldMapper = worldMapper;
    this.worldOverviews = Maps.newHashMap();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void loadWorlds() {
    try {
      this.convertWorldSummaries();
      this.loaded = true;
    } catch (AnvilConverterException e) {
      e.printStackTrace();
      this.loaded = false;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<WorldOverview> getWorlds() {
    if (this.loaded) {
      this.loaded = false;
      return new ArrayList<>(this.worldOverviews.values());
    }
    throw new WorldLoadException("The worlds are not loaded yet.");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canLoadWorld(String fileName) {
    return Minecraft.getInstance().getSaveLoader().canLoadWorld(fileName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLoaded() {
    return this.loaded;
  }

  private void convertWorldSummaries() throws AnvilConverterException {
    SaveFormat saveFormat = Minecraft.getInstance().getSaveLoader();
    for (WorldSummary worldSummary : saveFormat.getSaveList()) {
      this.worldOverviews.put(
          worldSummary.getFileName(),
          this.worldMapper.fromMinecraftWorldSummary(worldSummary)
      );
    }
  }

}
