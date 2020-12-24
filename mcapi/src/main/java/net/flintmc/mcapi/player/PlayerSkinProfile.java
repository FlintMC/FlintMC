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

package net.flintmc.mcapi.player;

import net.flintmc.mcapi.player.type.model.SkinModel;
import net.flintmc.mcapi.resources.ResourceLocation;

/**
 * Represents all skin-related things of a player.
 */
public interface PlayerSkinProfile {

  /**
   * Retrieves the skin model of this player
   *
   * @return The skin model of this player
   */
  SkinModel getSkinModel();

  /**
   * Retrieves the location of the player's skin
   *
   * @return The skin location
   */
  ResourceLocation getSkinLocation();

  /**
   * Retrieves the location of the player's cloak
   *
   * @return The cloak location
   */
  ResourceLocation getCloakLocation();

  /**
   * Retrieves the location of the player's elytra
   *
   * @return The elytra location
   */
  ResourceLocation getElytraLocation();

  /**
   * Whether the player has a skin.
   *
   * @return {@code true} if this player has a skin, otherwise {@code false}
   */
  boolean hasSkin();

  /**
   * Whether the player has a cloak.
   *
   * @return {@code true} if this player has a skin, otherwise {@code false}
   */
  boolean hasCloak();

  /**
   * Whether the player has a elytra.
   *
   * @return {@code true} if this player has a skin, otherwise {@code false}
   */
  boolean hasElytra();
}
