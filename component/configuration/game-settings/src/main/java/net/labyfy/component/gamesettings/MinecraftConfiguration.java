package net.labyfy.component.gamesettings;

import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.config.annotation.ExcludeStorage;
import net.labyfy.component.config.annotation.implemented.ImplementedConfig;
import net.labyfy.component.gamesettings.configuration.*;
import net.labyfy.component.settings.annotation.ui.NativeSetting;
import net.labyfy.component.world.difficult.Difficulty;

/**
 * Represents the Minecraft game settings
 */
@Config
@ImplementedConfig
@ExcludeStorage("local")
@NativeSetting
public interface MinecraftConfiguration {

  /**
   * Retrieves the accessibility configuration.
   *
   * @return The configuration of the accessibility.
   */
  AccessibilityConfiguration getAccessibilityConfiguration();

  /**
   * Retrieves the chat configuration.
   *
   * @return The configuration of the chat.
   */
  ChatConfiguration getChatConfiguration();

  /**
   * Retrieves the debug configuration.
   *
   * @return The configuration of debug.
   */
  DebugConfiguration getDebugConfiguration();

  /**
   * Retrieves the graphic configuration.
   *
   * @return The configuration of the graphics.
   */
  GraphicConfiguration getGraphicConfiguration();

  /**
   * Retrieves the key binding configuration.
   *
   * @return The configuration of key bindings.
   */
  //KeyBindingConfiguration getKeyBindingConfiguration();

  /**
   * Retrieves the mouse configuration.
   *
   * @return The configuration of the mouse.
   */
  MouseConfiguration getMouseConfiguration();

  /**
   * Retrieves the resource pack configuration.
   *
   * @return The configuration of the resource pack.
   */
  ResourcePackConfiguration getResourcePackConfiguration();

  /**
   * Retrieves the skin configuration.
   *
   * @return The configuration of the skin.
   */
  SkinConfiguration getSkinConfiguration();

  /**
   * Retrieves the sound configuration.
   *
   * @return The configuration of sounds.
   */
  SoundConfiguration getSoundConfiguration();

  /**
   * Whether the RealMS notifications are displayed.
   *
   * @return {@code true} if the RealMS notifications are displayed, otherwise {@code false}.
   */
  boolean isRealmsNotifications();

  /**
   * Changes the state whether the RealMS notifications are displayed.
   *
   * @param realmsNotifications The new state.
   */
  void setRealmsNotifications(boolean realmsNotifications);

  /**
   * Retrieves the difficulty.
   *
   * @return The current difficulty.
   */
  Difficulty getDifficulty();

  /**
   * Changes the difficulty.
   *
   * @param difficulty The new difficulty.
   */
  void setDifficulty(Difficulty difficulty);

  /**
   * Saves and reload the Minecraft options.
   * <p>
   * <b>Note:</b> This is needed to save configurations that have not been changed via the Minecraft settings.
   * The changes to the configuration will be loaded immediately so that the client can use the changes.
   */
  void saveAndReloadOptions();

}