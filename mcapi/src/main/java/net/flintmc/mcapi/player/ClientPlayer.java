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
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.player.PlayerInventory;
import net.flintmc.mcapi.player.overlay.TabOverlay;
import net.flintmc.mcapi.world.raytrace.RayTraceResult;

public interface ClientPlayer extends PlayerSkinProfile, PlayerEntity, BaseClientPlayer {

  /**
   * Retrieves the inventory of this player.
   *
   * @return The player's inventory.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  PlayerInventory getInventoryController();

  /**
   * Retrieves the opened inventory of this player.
   *
   * @return The opened inventory.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  Inventory getOpenInventory();

  /**
   * Prints a message into the player chat.
   *
   * @param message The message to print.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void sendChatMessage(String message);

  /**
   * Closes the screen and drop an item stack.
   *
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void closeScreenAndDropStack();

  /**
   * Changes the health of this player.
   *
   * <p>This is only on the client side.
   *
   * @param health The new health.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setPlayerSPHealth(float health);

  /**
   * Sends the horse inventory to the server.
   *
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void sendHorseInventory();

  /**
   * Retrieves the server brand
   *
   * @return The server brand.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  String getServerBrand();

  /**
   * Changes the server brand.
   *
   * @param serverBrand The new server brand.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setServerBrand(String serverBrand);

  /**
   * Changes the permission level of this player.
   *
   * @param level The new permission level.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setPermissionLevel(int level);

  /**
   * Sets the experience stats of this player.
   *
   * @param currentExperience The current experience of this player.
   * @param maxExperience     The maximal experience of this player.
   * @param level             The level of this player.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setExperienceStats(float currentExperience, int maxExperience, int level);

  /**
   * Whether the player is show a death screen.
   *
   * @return {@code true} if the player is show a death screen, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isShowDeathScreen();

  /**
   * Changes the state whether the death screen is displayed.
   *
   * @param showDeathScreen {@code true} if the death screen should be displayed, otherwise {@code
   *                        false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setShowDeathScreen(boolean showDeathScreen);

  /**
   * Whether the player is riding a horse.
   *
   * @return {@code true} if the player is riding a horse, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isRidingHorse();

  /**
   * Retrieves the horse jump power.
   *
   * @return The jump power of the horse.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getHorseJumpPower();

  /**
   * Whether the player is rowing with a boat.
   *
   * @return {@code true} if the player is rowing with a boat, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isRowingBoat();

  /**
   * Whether auto jump is enabled.
   *
   * @return {@code true} if the auto jump is enabled, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isAutoJumpEnabled();

  /**
   * Retrieves the water brightness of this player.
   *
   * @return The water brightness.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getWaterBrightness();

  /**
   * Retrieves the tab overlay of this player.
   *
   * @return The tab overlay.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  TabOverlay getTabOverlay();

  /**
   * Retrieves the render arm yaw of this player entity.
   *
   * @return The player entity render arm yaw.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getRenderArmYaw();

  /**
   * Changes the render arm yaw of this player entity.
   *
   * @param renderArmYaw The new render arm yaw.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setRenderArmYaw(float renderArmYaw);

  /**
   * Retrieves the previous render arm yaw of this player entity.
   *
   * @return The player entity previous render arm yaw.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getPreviousRenderArmYaw();

  /**
   * Changes the previous render arm yaw of this player entity.
   *
   * @param previousRenderArmYaw The new previous render arm yaw.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setPreviousArmYaw(float previousRenderArmYaw);

  /**
   * Retrieves the previous render arm pitch of this player entity.
   *
   * @return The player entity previous render arm pitch.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getRenderArmPitch();

  /**
   * Changes the render arm pitch of this player entity.
   *
   * @param renderArmPitch The new render arm pitch.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setRenderArmPitch(float renderArmPitch);

  /**
   * Retrieves the previous render arm pitch of this player entity.
   *
   * @return The player entity previous render arm pitch.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getPreviousRenderArmPitch();

  /**
   * Changes the previous render arm pitch of this player entity.
   *
   * @param previousRenderArmPitch The new previous render arm pitch.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setPreviousRenderArmPitch(float previousRenderArmPitch);

  /**
   * Retrieves the current biome name of this player.
   *
   * @return Player's current biome name.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  String getBiome();

  /**
   * Retrieves the entity/block that is currently targeted by the player. For the object to be
   * detected as it is targeted by the player, the player has to look at it and it needs to be
   * within a specific range.
   *
   * @return The non-null object that is targeted by the player
   */
  RayTraceResult getTargetedObject();

  /**
   * Retrieves whether this player is a spectator.
   *
   * @return {@code true} if this player is a spectator, {@code false} otherwise
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  @Override
  default boolean isSpectator() {
    return false;
  }
}
