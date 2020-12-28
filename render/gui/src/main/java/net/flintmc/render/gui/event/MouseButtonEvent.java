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

/** Event indicating that a mouse button state has changed or is still being hold. */
@Subscribable(Phase.PRE)
public class MouseButtonEvent extends EventWithModifierKeys {
  private final Key button;
  private final InputState state;
  private final double x;
  private final double y;

  /**
   * Constructs a new {@link MouseButtonEvent} with the given state, button and modifier keys.
   *
   * @param window The non-null window where this event has happened
   * @param state The new state the button is in
   * @param button The mouse button that has changed state with {@link Key#isMouse()} being
   *     {@code true}
   * @param x The x coordinate of the event
   * @param y The y coordinate of the event
   * @param modifierKeys The modifier keys which were active while the event was fired
   */
  public MouseButtonEvent(
      Window window,
      Key button,
      InputState state,
      double x,
      double y,
      Set<ModifierKey> modifierKeys) {
    super(window, modifierKeys);
    this.button = button;
    this.state = state;
    this.x = x;
    this.y = y;
  }

  /**
   * Retrieves the new state of the mouse button
   *
   * @return The new state of the button
   */
  public InputState getState() {
    return state;
  }

  /**
   * Retrieves the mouse button that has changed state.
   *
   * <p>Note: {@link Key#isMouse()} will always be {@code true} for the returned value
   *
   * @return The button that has changed state
   */
  public Key getButton() {
    return button;
  }

  /**
   * Retrieves the x coordinate of the event.
   *
   * @return The x coordinate
   */
  public double getX() {
    return x;
  }

  /**
   * Retrieves the y coordinate of the event.
   *
   * @return The y coordinate
   */
  public double getY() {
    return y;
  }
}
