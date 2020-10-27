package net.flintmc.mcapi.gamesettings.configuration;

import net.flintmc.mcapi.chat.Keybind;
import net.flintmc.mcapi.gamesettings.KeyBindMappings;
import net.flintmc.mcapi.gamesettings.KeyBinding;

import java.util.List;

/**
 * Represents the key binding configuration.
 */
public interface KeyBindingConfiguration {

  /**
   * Retrieves a key binding by the given {@link Keybind} constant.
   *
   * @param keybind The non-null constant of the {@link Keybind}.
   * @return A key binding by the given constant or the default `jump` key binding.
   */
  KeyBinding getKeyBinding(Keybind keybind);

  /**
   * Retrieves a collection with all key bindings for the hotbar.
   *
   * @return A collection with all key binding for the hotbar.
   */
  List<KeyBinding> getKeyBindsHotbar();

  /**
   * Retrieves a collection with all registered key bindings.
   *
   * @return A collection with all registered key bindings.
   */
  List<KeyBinding> getKeyBindings();

  /**
   * Changes the binding of a key.
   *
   * @param keyBinding The key binding to change.
   * @param code       The new code for key binding.
   */
  void setKeyBindingCode(KeyBinding keyBinding, KeyBindMappings code);

}
