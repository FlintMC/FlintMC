package net.labyfy.component.gamesettings.configuration;

import net.labyfy.component.config.annotation.implemented.ImplementedConfig;
import net.labyfy.component.player.type.hand.Hand;
import net.labyfy.component.player.type.model.PlayerClothing;
import net.labyfy.component.settings.annotation.Component;
import net.labyfy.component.settings.annotation.ui.DefineCategory;
import net.labyfy.component.settings.options.dropdown.EnumDropDownSetting;

import java.util.Map;
import java.util.Set;

/**
 * Represents the skin configuration.
 */
@DefineCategory(
    name = "minecraft.settings.skin",
    displayName = @Component(value = "minecraft.settings.skin.display", translate = true),
    description = @Component(value = "minecraft.settings.skin.description", translate = true)
)
@ImplementedConfig
public interface SkinConfiguration {

  /**
   * Retrieves the main hand side.
   *
   * @return The main hand side.
   */
  @EnumDropDownSetting(defaultValue = 1 /*`RIGHT */)
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
  // TODO for booleans like the one in setModelClothingEnabled(PlayerClothing,boolean), a set should also work instead of the map below

  Map<PlayerClothing, Boolean> getAllModelClothingEnabled();

  /**
   * Changes the state of the player clothing.
   *
   * @param clothing The player clothing to enable or disable.
   * @param state    {@code true} if the clothing should be enabled, otherwise {@code false}.
   */
  void setModelClothingEnabled(PlayerClothing clothing, boolean state);

  void setAllModelClothingEnabled(Map<PlayerClothing, Boolean> map);

  boolean isModelClothingEnabled(PlayerClothing clothing);

  /**
   * Switches the state of the given player clothing.
   *
   * @param clothing The player clothing to switch the state.
   */
  void switchModelClothingEnabled(PlayerClothing clothing);

}
