package net.flintmc.mcapi.settings.game.configuration;

import net.flintmc.framework.config.annotation.ConfigExclude;
import net.flintmc.framework.config.annotation.implemented.ImplementedConfig;
import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.player.type.model.PlayerClothing;
import net.flintmc.mcapi.settings.flint.annotation.ui.DefineCategory;
import net.flintmc.mcapi.settings.flint.options.dropdown.EnumSelectSetting;

import java.util.Set;

/** Represents the skin configuration. */
@DefineCategory(
    name = "minecraft.settings.skin",
    displayName = @Component(value = "options.skinCustomisation", translate = true))
@ImplementedConfig
public interface SkinConfiguration {

  /**
   * Retrieves the main hand side.
   *
   * @return The main hand side.
   */
  @EnumSelectSetting
  Hand.Side getMainHand();

  /**
   * Changes the main hand side.
   *
   * @param mainHand The new main hand side.
   */
  void setMainHand(Hand.Side mainHand);

  /**
   * Retrieves a collection with all enabled player clothing.
   *
   * @return A collection with all enabled player clothing.
   */
  @ConfigExclude
  Set<PlayerClothing> getPlayerClothing();

  /**
   * Changes the state of the player clothing.
   *
   * @param clothing The player clothing to enable or disable.
   * @param state {@code true} if the clothing should be enabled, otherwise {@code false}.
   */
  void setModelClothingEnabled(PlayerClothing clothing, boolean state);

  /**
   * Retrieves whether a clothing is enabled.
   *
   * @param clothing The non-null clothing to check for
   * @return {@code true} if the clothing is enabled, {@code false} otherwise
   */
  boolean isModelClothingEnabled(PlayerClothing clothing);

  /**
   * Switches the state of the given player clothing.
   *
   * @param clothing The player clothing to switch the state.
   */
  void switchModelClothingEnabled(PlayerClothing clothing);
}
