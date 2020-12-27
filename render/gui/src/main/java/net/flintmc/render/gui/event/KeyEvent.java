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

package net.flintmc.render.gui.event;

import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.render.gui.input.InputState;
import net.flintmc.render.gui.input.ModifierKey;
import net.flintmc.render.gui.input.Key;
import net.flintmc.render.gui.windowing.Window;

import java.util.Set;

/** Event indicating that a key state has changed or is still being hold. */
@Subscribable(Phase.PRE)
public class KeyEvent extends EventWithModifierKeys {
  private final Key key;
  private final int scancode;
  private final InputState state;

  /**
   * Constructs a new {@link KeyEvent} with the given key, scancode, state and modifier keys.
   *
   * @param window The non-null window where this event has happened
   * @param key The key that has changed state with {@link Key#isMouse()} being {@code
   *     false}
   * @param scancode The (system specific) scancode of the key that has changed
   * @param state The new state of the key
   * @param modifierKeys The modifier keys which were active while the event was fired
   */
  public KeyEvent(
      Window window,
      Key key,
      int scancode,
      InputState state,
      Set<ModifierKey> modifierKeys) {
    super(window, modifierKeys);
    this.key = key;
    this.scancode = scancode;
    this.state = state;
  }

  /**
   * Retrieves the key that has changed state.
   *
   * <p>Note: {@link Key#isMouse()} will always be {@code false} for the returned value
   *
   * @return The key that has changed state
   */
  public Key getKey() {
    return key;
  }

  /**
   * Retrieves the (system specific) scancode of the key that has changed state.
   *
   * @return The scancode of the key that has changed state
   */
  public int getScancode() {
    return scancode;
  }

  /**
   * Retrieves the new state of the key.
   *
   * @return The new state of the key
   */
  public InputState getState() {
    return state;
  }
}
