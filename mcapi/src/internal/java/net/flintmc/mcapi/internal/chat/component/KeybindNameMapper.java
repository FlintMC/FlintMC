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

package net.flintmc.mcapi.internal.chat.component;

import net.flintmc.mcapi.chat.Keybind;

/**
 * Mapper from {@link Keybind} to the translated string.
 */
public interface KeybindNameMapper {

  /**
   * Maps the given keybind to the translation of the selected key for the given keybind of
   * Minecraft.
   *
   * @param keybind The non-null keybind to be mapped
   * @return The non-null mapped text of the given keybind, empty if no key is bound
   */
  String translateKeybind(Keybind keybind);

}
