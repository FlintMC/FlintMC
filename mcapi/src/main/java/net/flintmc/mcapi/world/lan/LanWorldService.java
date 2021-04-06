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

package net.flintmc.mcapi.world.lan;

/**
 * Service for opening Singleplayer worlds to the LAN.
 */
public interface LanWorldService {

  /**
   * Retrieves whether the currently loaded world can be opened to the LAN. This includes being in a
   * Singleplayer world and not having the world already opened to the LAN.
   *
   * @return {@code true} if it can be opened to the LAN, {@code false} otherwise
   */
  boolean isAvailable();

  /**
   * Opens the currently loaded world to the LAN so that other players can join it. The behavior may
   * be modified with the {@link LanWorldOpenEvent}.
   *
   * @param options The non-null options to be used to start the LAN world
   * @return {@code true} if the world was successfully opened to the LAN, {@code false} otherwise
   */
  boolean openLanWorld(LanWorldOptions options);

}
