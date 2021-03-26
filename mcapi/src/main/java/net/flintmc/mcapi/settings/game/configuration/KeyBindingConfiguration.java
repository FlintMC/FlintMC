/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.settings.game.configuration;

import net.flintmc.framework.config.annotation.implemented.ImplementedConfig;
import net.flintmc.mcapi.chat.Keybind;
import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.annotation.TranslateKey;
import net.flintmc.mcapi.settings.flint.annotation.ui.DefineCategory;
import net.flintmc.mcapi.settings.game.keybind.KeyBindSetting;
import net.flintmc.render.gui.input.Key;
import java.util.Map;

/**
 * Represents the key binding configuration.
 */
@DefineCategory(
    name = "minecraft.settings.controls",
    displayName = @Component(value = "options.controls", translate = true))
@ImplementedConfig
public interface KeyBindingConfiguration {

  /**
   * Retrieves the physically bound key to a specific description.
   *
   * @param keyDescription The non-null description of the key ({@link Keybind#getKey()})
   * @return The key that is bound to the given description or {@code null}, if no key is bound
   */
  @KeyBindSetting
  Key getKey(String keyDescription);

  /**
   * Binds the physical key to a specific description
   *
   * @param keyDescription The non-null description of the key ({@link Keybind#getKey()})
   * @param key            The key to be bound, {@code null} to disable the binding
   */
  void setKey(String keyDescription, Key key);

  /**
   * Retrieves a map of all key descriptions with their bound physical key.
   *
   * <p>Modification to this map won't have any effect.
   *
   * @return The new non-null map containing all key descriptions with their bound keys
   * @see #getKey(String)
   */
  @TranslateKey
  Map<String, Key> getAllKey();

  /**
   * Binds multiple key descriptions to a specific physical key.
   *
   * @param keys The non-null map containing all key descriptions with their bound keys
   */
  void setAllKey(Map<String, Key> keys);
}
