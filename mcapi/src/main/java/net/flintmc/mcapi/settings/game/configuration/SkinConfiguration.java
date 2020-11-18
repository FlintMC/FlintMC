package net.flintmc.mcapi.settings.game.configuration;

import net.flintmc.framework.config.annotation.implemented.ImplementedConfig;
import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.player.type.model.PlayerClothing;
import net.flintmc.mcapi.settings.flint.annotation.ui.DefineCategory;
import net.flintmc.mcapi.settings.flint.options.dropdown.EnumSelectSetting;

import java.util.HashSet;
import java.util.Map;
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
  default Set<PlayerClothing> getPlayerClothing() {
    Set<PlayerClothing> set = new HashSet<>();
    this.getAllModelClothingEnabled()
        .forEach(
            (clothing, value) -> {
              if (value) {
                set.add(clothing);
              }
            });
    return set;
  }

  /**
   * Retrieves a map with all player clothing and whether they are enabled or not.
   *
   * @return A map with all player clothing
   */
  Map<PlayerClothing, Boolean> getAllModelClothingEnabled();

  /**
   * Sets all player clothing enabled/disabled. If a clothing is not available in the map, it is the
   * same as if the value would be false.
   *
   * @param map The map containing all clothing and their value
   */
  void setAllModelClothingEnabled(Map<PlayerClothing, Boolean> map);

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