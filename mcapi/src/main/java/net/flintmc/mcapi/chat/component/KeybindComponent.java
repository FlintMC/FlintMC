package net.flintmc.mcapi.chat.component;

import net.flintmc.mcapi.chat.Keybind;

/**
 * A component of the chat which allows to display the name of the key out of the settings from the
 * player.
 */
public interface KeybindComponent extends ChatComponent {

  /**
   * Retrieves the keybind which will be replaced with the configured key in the settings by the
   * client.
   *
   * @return The keybind out of this component or {@code null} if no keybind has been set
   * @see #keybind(Keybind)
   */
  Keybind keybind();

  /**
   * Sets the new keybind for this component. The client will replace this with the key specified in
   * their settings.
   *
   * @param keybind The new non-null keybind
   */
  void keybind(Keybind keybind);
}
