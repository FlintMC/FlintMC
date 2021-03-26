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

import net.flintmc.mcapi.chat.Keybind;
import net.flintmc.render.gui.input.Key;

import java.util.List;

public interface KeyBindingList {

  /**
   * Retrieves whether there are duplicates Minecrafts settings for this keyCode.
   *
   * @return {@code true} if other KeyBindings in Minecraft also use this keyCode, {@code false}
   * otherwise
   */
  boolean hasDuplicates(Key key);

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

}
