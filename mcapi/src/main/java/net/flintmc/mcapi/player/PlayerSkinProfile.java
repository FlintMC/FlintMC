package net.flintmc.mcapi.player;

import net.flintmc.mcapi.player.type.model.SkinModel;
import net.flintmc.mcapi.resources.ResourceLocation;

/** Represents all skin-related things of a player. */
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
