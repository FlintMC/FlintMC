package net.flintmc.mcapi.player;

import net.flintmc.mcapi.player.network.NetworkPlayerInfo;

/**
 * Represents the Minecraft abstract client player.
 */
public interface BaseClientPlayer extends PlayerSkinProfile {

  /**
   * Retrieves the pitch of the elytra.
   *
   * @return The elytra pitch.
   */
  float getElytraPitch();

  /**
   * Changes the pitch of the elytra.
   *
   * @param elytraPitch The new elytra pitch.
   */
  void setElytraPitch(float elytraPitch);

  /**
   * Retrieves the yaw of the elytra.
   *
   * @return The elytra yaw.
   */
  float getElytraYaw();

  /**
   * Changes the yaw of the elytra.
   *
   * @param elytraYaw The new elytra yaw.
   */
  void setElytraYaw(float elytraYaw);

  /**
   * Retrieves the roll of the elytra.
   *
   * @return The elytra roll.
   */
  float getElytraRoll();

  /**
   * Changes the roll of the elytra.
   *
   * @param elytraRoll The new elytra roll.
   */
  void setElytraRoll(float elytraRoll);

  /**
   * Whether the entity is a spectator.
   *
   * @return {@code true} if the entity is a spectator, otherwise {@code false}.
   */
  boolean isSpectator();

  /**
   * Whether the entity is in creative mode.
   *
   * @return {@code true} if the entity is in the creative mode, otherwise {@code false}.
   */
  boolean isCreative();

  /**
   * Whether the player has network information.
   *
   * @return {@code true} if the player has network information, otherwise {@code false}.
   */
  boolean hasPlayerInfo();

  /**
   * Retrieves the network player information of this player.
   *
   * @return The player's network information.
   */
  NetworkPlayerInfo getNetworkPlayerInfo();

  /**
   * Retrieves the modifier of this player's fov.
   *
   * @return The FOV modifier.
   */
  float getFovModifier();

}
