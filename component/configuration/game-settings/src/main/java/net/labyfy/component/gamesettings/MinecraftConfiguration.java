package net.labyfy.component.gamesettings;

import net.labyfy.component.gamesettings.configuration.*;

/**
 * Represents the Minecraft game settings
 */
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
  KeyBindingConfiguration getKeyBindingConfiguration();

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
  // TODO: 28.09.2020 Wait for merge request #177 (Difficulty)
  Object getDifficulty();

  /**
   * Changes the difficulty.
   *
   * @param difficulty The new difficulty.
   */
  // TODO: 28.09.2020 Wait for merge request #177 (Difficulty)
  void setDifficulty(Object difficulty);

}