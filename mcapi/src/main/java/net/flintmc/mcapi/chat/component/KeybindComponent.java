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

package net.flintmc.mcapi.chat.component;

import net.flintmc.framework.inject.assisted.AssistedFactory;
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

  /**
   * Factory for the {@link KeybindComponent}.
   */
  @AssistedFactory(KeybindComponent.class)
  interface Factory extends ChatComponent.Factory<KeybindComponent> {

  }
}
