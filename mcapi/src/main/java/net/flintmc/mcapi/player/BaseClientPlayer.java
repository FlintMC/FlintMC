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

import net.flintmc.mcapi.entity.EntityNotLoadedException;
import net.flintmc.mcapi.player.network.NetworkPlayerInfo;

/**
 * Represents the Minecraft abstract client player.
 */
public interface BaseClientPlayer extends PlayerSkinProfile {

  /**
   * Retrieves the pitch of the elytra.
   *
   * @return The elytra pitch.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getElytraPitch();

  /**
   * Changes the pitch of the elytra.
   *
   * @param elytraPitch The new elytra pitch.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setElytraPitch(float elytraPitch);

  /**
   * Retrieves the yaw of the elytra.
   *
   * @return The elytra yaw.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getElytraYaw();

  /**
   * Changes the yaw of the elytra.
   *
   * @param elytraYaw The new elytra yaw.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setElytraYaw(float elytraYaw);

  /**
   * Retrieves the roll of the elytra.
   *
   * @return The elytra roll.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getElytraRoll();

  /**
   * Changes the roll of the elytra.
   *
   * @param elytraRoll The new elytra roll.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setElytraRoll(float elytraRoll);

  /**
   * Whether the entity is a spectator.
   *
   * @return {@code true} if the entity is a spectator, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isSpectator();

  /**
   * Whether the entity is in creative mode.
   *
   * @return {@code true} if the entity is in the creative mode, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isCreative();

  /**
   * Whether the player has network information.
   *
   * @return {@code true} if the player has network information, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean hasPlayerInfo();

  /**
   * Retrieves the network player information of this player.
   *
   * @return The player's network information.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  NetworkPlayerInfo getNetworkPlayerInfo();

  /**
   * Retrieves the modifier of this player's fov.
   *
   * @return The FOV modifier.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getFovModifier();
}
