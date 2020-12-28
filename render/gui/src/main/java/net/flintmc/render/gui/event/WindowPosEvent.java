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

/** Event indicating that the window position has changed. */
@Subscribable(Phase.PRE)
public class WindowPosEvent extends DefaultGuiEvent implements GuiEvent {
  private final int x;
  private final int y;

  /**
   * Constructs a new {@link WindowPosEvent} with the specified x and y coordinates.
   *
   * @param window The non-null window where this event has happened
   * @param x The new x coordinate of the window
   * @param y The new y coordinate of the window
   */
  public WindowPosEvent(Window window, int x, int y) {
    super(window);
    this.x = x;
    this.y = y;
  }

  /**
   * Retrieves the new x coordinate of the window.
   *
   * @return The new x coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Retrieves the new y coordinate of the window.
   *
   * @return The new y coordinate
   */
  public int getY() {
    return y;
  }
}
