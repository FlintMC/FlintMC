package net.labyfy.component.gamesettings.configuration;

import net.labyfy.chat.Keybind;
import net.labyfy.chat.annotation.Component;
import net.labyfy.component.config.annotation.implemented.ImplementedConfig;
import net.labyfy.component.gamesettings.KeyBinding;
import net.labyfy.component.settings.annotation.ui.DefineCategory;
import net.labyfy.component.settings.options.keybind.PhysicalKey;

import java.util.List;

/**
 * Represents the key binding configuration.
 */
@DefineCategory(
    name = "minecraft.settings.controls",
    displayName = @Component(value = "minecraft.settings.controls.display", translate = true),
    description = @Component(value = "minecraft.settings.controls.description", translate = true)
)
@ImplementedConfig

// TODO add settings annotations

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
  void setKeyBindingCode(KeyBinding keyBinding, PhysicalKey code);

}
