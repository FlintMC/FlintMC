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

package net.flintmc.mcapi.settings.game.keybind;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.render.gui.input.Key;

/**
 * Represents a key binding.
 */
public interface KeyBinding {

  /**
   * Retrieves the key category of the key binding.
   *
   * @return The key category.
   */
  String getKeyCategory();

  /**
   * Retrieves the key description of the key binding.
   *
   * @return The key description.
   */
  String getKeyDescription();

  /**
   * Retrieves the key code of the key binding.
   *
   * @return The key code.
   */
  int getKeyCode();

  /**
   * Whether the key binding is pressed.
   *
   * @return {@code true} if the key binding is pressed.
   */
  boolean isPressed();

  /**
   * Changes the state whether the key binding is pressed.
   *
   * @param pressed The new state.
   */
  void setPressed(boolean pressed);

  /**
   * Binds the key binding to a key.
   *
   * @param key The new key for the binding.
   */
  void bind(Key key);

  /**
   * Whether the key binding is invalid.
   *
   * @return {@code true} if the key binding is invalid, otherwise {@code false}.
   */
  boolean isInvalid();

  /**
   * Checks if the given parameters match the values in the key binding.
   *
   * @param keyCode  The key code.
   * @param scanCode The scan code.
   * @return {@code true} if the given parameters match the values, otherwise {@code false}.
   */
  boolean matchesKey(int keyCode, int scanCode);

  /**
   * Checks if the given parameter match the value in the key binding.
   *
   * @param key The mouse key.
   * @return {@code true} if the given parameters match the values, otherwise {@code false}.
   */
  boolean matchesMouseKey(int key);

  /**
   * Retrieves the localized name of this key binding.
   *
   * @return The localized name.
   */
  String getLocalizedName();

  /**
   * Whether the key binding is default.
   *
   * @return {@code true} if the key binding is default, otherwise {@code false}.
   */
  boolean isDefault();

  /**
   * Retrieves the translation key of the key binding.
   *
   * @return The translation key of the key binding.
   */
  String getTranslationKey();

  /**
   * A factory class for the {@link KeyBinding}.
   */
  @AssistedFactory(KeyBinding.class)
  interface Factory {

    /**
     * Creates a new {@link KeyBinding} with the given parameters.
     *
     * @param description The key binding description.
     * @param keyCode     The code of the key binding.
     * @param category    The category of the key binding.
     * @return A created key binding.
     */
    KeyBinding create(
        @Assisted("description") String description,
        @Assisted("keyCode") int keyCode,
        @Assisted("category") String category);
  }
}
