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

package net.flintmc.mcapi.debug;

import java.util.function.BooleanSupplier;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.render.gui.input.Key;

/**
 * Helper class for various minecraft debugging utilities.
 */
public interface MinecraftDebugger {

  /**
   * Registers a new hook which is executed before action associated with the debug key. This is
   * meant to extend already present debug keybindings. The key chosen here must be an already
   * registered keybinding.
   * <p>
   * See {@link #registerDebugKeybinding(Key, ChatComponent, BooleanSupplier)} for a way of
   * registering or overwriting keybindings.
   *
   * @param key      The key to register the hook for
   * @param callback The callback to execute as the hook
   */
  void registerExecutionHook(Key key, Runnable callback);

  /**
   * Registers a new debug keybinding which can then be used in combination with F3.
   *
   * @param key         The key which is used in combination with F3
   * @param description The description to display in the chat
   * @param callback    The callback to execute when the keybinding is triggered, returns {@code
   *                    true} to signal that the key has been handled, or {@code false}, to signal
   *                    that the key has not been handled
   */
  void registerDebugKeybinding(Key key, ChatComponent description, BooleanSupplier callback);
}
