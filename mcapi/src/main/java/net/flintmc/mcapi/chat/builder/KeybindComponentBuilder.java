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

package net.flintmc.mcapi.chat.builder;

import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.Keybind;
import net.flintmc.mcapi.chat.component.KeybindComponent;

/**
 * Builder for {@link KeybindComponent}s.
 */
public interface KeybindComponentBuilder extends ComponentBuilder<KeybindComponentBuilder> {

  /**
   * Sets the keybind of the current component. Every component can only have one keybind, so this
   * overrides any calls that have been done before to this method.
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

  /**
   * Factory for the {@link KeybindComponentBuilder}.
   */
  @AssistedFactory(KeybindComponentBuilder.class)
  interface Factory {

    /**
     * Creates a new {@link KeybindComponentBuilder} without any arguments.
     *
     * @return The new non-null {@link KeybindComponentBuilder}
     */
    KeybindComponentBuilder newBuilder();

  }

}
