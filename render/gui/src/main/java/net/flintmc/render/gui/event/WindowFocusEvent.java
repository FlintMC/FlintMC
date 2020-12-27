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
import net.flintmc.render.gui.windowing.Window;

/** Event indicating that a window got or lost focus. */
@Subscribable(Phase.PRE)
public class WindowFocusEvent extends DefaultGuiEvent implements GuiEvent {
  private final boolean isFocused;

  /**
   * Constructs a new {@link WindowFocusEvent} with the specified focus state.
   *
   * @param window The non-null window where this event has happened
   * @param isFocused The new focus state of the window
   */
  public WindowFocusEvent(Window window, boolean isFocused) {
    super(window);
    this.isFocused = isFocused;
  }

  /**
   * Determines whether the window is focused or unfocused now.
   *
   * @return {@code true} if the window is focused now, {@code false} otherwise
   */
  public boolean isFocused() {
    return isFocused;
  }
}
