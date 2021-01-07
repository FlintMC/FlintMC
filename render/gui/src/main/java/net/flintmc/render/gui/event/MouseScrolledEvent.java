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

/**
 * Event indicating that the user has done scroll input
 */
@Subscribable(Phase.PRE)
public class MouseScrolledEvent extends DefaultGuiEvent implements GuiEvent {

  private final double xOffset;
  private final double yOffset;

  /**
   * Constructs a new {@link MouseScrolledEvent} with the given offsets
   *
   * @param window  The non-null window where this event has happened
   * @param xOffset The amount the user has scrolled on the x axis
   * @param yOffset The amount the user has scrolled on the y axis
   */
  public MouseScrolledEvent(Window window, double xOffset, double yOffset) {
    super(window);
    this.xOffset = xOffset;
    this.yOffset = yOffset;
  }

  /**
   * Retrieves the amount the user has scrolled on the x axis
   *
   * @return Amount the user has scrolled horizontally
   */
  public double getXOffset() {
    return xOffset;
  }

  /**
   * Retrieves the amount the user has scrolled on the y axis
   *
   * @return Amount the user has scrolled vertically
   */
  public double getYOffset() {
    return yOffset;
  }
}
