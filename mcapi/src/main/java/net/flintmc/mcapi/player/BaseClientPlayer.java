package net.flintmc.mcapi.player;

import net.flintmc.mcapi.entity.EntityNotLoadedException;
import net.flintmc.mcapi.player.network.NetworkPlayerInfo;

/** Represents the Minecraft abstract client player. */
public interface BaseClientPlayer extends PlayerSkinProfile {

  /**
   * Retrieves the pitch of the elytra.
   *
   * @return The elytra pitch.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *     client
   */
  float getElytraPitch();

  /**
   * Changes the pitch of the elytra.
   *
   * @param elytraPitch The new elytra pitch.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *     client
   */
  void setElytraPitch(float elytraPitch);

  /**
   * Retrieves the yaw of the elytra.
   *
   * @return The elytra yaw.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *     client
   */
  float getElytraYaw();

  /**
   * Changes the yaw of the elytra.
   *
   * @param elytraYaw The new elytra yaw.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *     client
   */
  void setElytraYaw(float elytraYaw);

  /**
   * Retrieves the roll of the elytra.
   *
   * @return The elytra roll.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *     client
   */
  float getElytraRoll();

  /**
   * Changes the roll of the elytra.
   *
   * @param elytraRoll The new elytra roll.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *     client
   */
  void setElytraRoll(float elytraRoll);

  /**
   * Whether the entity is a spectator.
   *
   * @return {@code true} if the entity is a spectator, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *     client
   */
  boolean isSpectator();

  /**
   * Whether the entity is in creative mode.
   *
   * @return {@code true} if the entity is in the creative mode, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *     client
   */
  boolean isCreative();

  /**
   * Whether the player has network information.
   *
   * @return {@code true} if the player has network information, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *     client
   */
  boolean hasPlayerInfo();

  /**
   * Retrieves the network player information of this player.
   *
   * @return The player's network information.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *     client
   */
  NetworkPlayerInfo getNetworkPlayerInfo();

  /**
   * Retrieves the modifier of this player's fov.
   *
   * @return The FOV modifier.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *     client
   */
  float getFovModifier();
}
