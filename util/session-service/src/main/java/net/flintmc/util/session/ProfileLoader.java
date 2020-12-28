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

package net.flintmc.util.session;

import java.util.UUID;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.gameprofile.property.PropertyMap;

/**
 * The ProfileLoader loads textures and player names for a specified UUID.
 */
public interface ProfileLoader {

  /**
   * Loads all properties that are available from the session server.
   *
   * @param uniqueId The non-null UUID of the player to load the textures for
   * @return The new non-null map with all properties (textures)
   */
  PropertyMap loadProfileProperties(UUID uniqueId);

  /**
   * Loads all properties that are available and the name of the player from the session server.
   *
   * @param uniqueId The non-null UUID of the player to load the textures and name for
   * @return The new non-null profile filled with the UUID, name and properties (textures)
   */
  GameProfile loadProfile(UUID uniqueId);
}
