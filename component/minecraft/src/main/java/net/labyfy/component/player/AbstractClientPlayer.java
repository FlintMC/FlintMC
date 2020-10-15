package net.labyfy.component.player;

import net.labyfy.component.player.network.NetworkPlayerInfo;

/**
 * Represents the Minecraft abstract client player.
 */
public interface AbstractClientPlayer extends PlayerSkinProfile {

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
