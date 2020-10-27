package net.flintmc.mcapi.gamesettings.configuration;

import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.player.type.model.PlayerClothing;

import java.util.Set;

/** Represents the skin configuration. */
public interface SkinConfiguration {

  /**
   * Retrieves the main hand side.
   *
   * @return The main hand side.
   */
  Hand.Side getMainHand();

  /**
   * Changes the main hand side.
   *
   * @param mainHand The new main hand side.
   */
  void setMainHand(Hand.Side mainHand);

  /**
   * Retrieves a collection with all player clothing.
   *
   * @return A collection with all player clothing.
   */
  Set<PlayerClothing> getPlayerClothing();

  /**
   * Changes the state of the player clothing.
   *
   * @param clothing The player clothing to enable or disable.
   * @param state {@code true} if the clothing should be enabled, otherwise {@code false}.
   */
  void setModelClothingEnabled(PlayerClothing clothing, boolean state);

  /**
   * Switches the state of the given player clothing.
   *
   * @param clothing The player clothing to switch the state.
   */
  void switchModelClothingEnabled(PlayerClothing clothing);
}
