package net.labyfy.chat.builder;

import net.labyfy.chat.Keybind;
import net.labyfy.chat.component.KeybindComponent;

/**
 * Builder for {@link KeybindComponent}s.
 */
public interface KeybindComponentBuilder extends ComponentBuilder<KeybindComponentBuilder> {

  /**
   * Sets the keybind of the current component. Every component can only have one keybind, so this overrides any calls
   * that have been done before to this method.
   *
   * @param keybind The new non-null keybind
   * @return this
   */
  KeybindComponentBuilder keybind(Keybind keybind);

  /**
   * Retrieves the keybind of the current component.
   *
   * @return The keybind out of the current component or {@code null} if no keybind has been set
   * @see #keybind(Keybind)
   */
  Keybind keybind();
}
