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

package net.flintmc.mcapi.world.storage.service;

import java.util.List;
import net.flintmc.mcapi.world.storage.WorldOverview;
import net.flintmc.mcapi.world.storage.service.exception.WorldLoadException;

/**
 * Represents a loader to load worlds.
 */
public interface WorldLoader {

  /**
   * Loads all saved worlds.
   */
  void loadWorlds();

  /**
   * Retrieves a collection with all loaded worlds.
   *
   * @return A collection with all loaded worlds.
   * @throws WorldLoadException Will be thrown if the worlds are not loaded.
   */
  List<WorldOverview> getWorlds();

  /**
   * Whether the world can be loaded.
   *
   * @param fileName The file name of the world to be loaded.
   * @return {@code true} if the world can be loaded, otherwise {@code false}.
   */
  boolean canLoadWorld(String fileName);

  /**
   * Whether the world loader has loaded the worlds.
   *
   * @return {@code true} if the world loader has loaded the worlds, otherwise {@code false}.
   */
  boolean isLoaded();

}
