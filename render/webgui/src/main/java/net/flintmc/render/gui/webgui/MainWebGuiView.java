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

package net.flintmc.render.gui.webgui;

/** Represents the {@link WebGuiView} inside the main minecraft window. */
public interface MainWebGuiView extends WebGuiView {
  /**
   * Enables or disables focus for this view. This is different from the window focus, as that both
   * the window focus and this need to be true in order for the view to gain focus.
   *
   * @param allow Whether focusing the view is allowed
   */
  void setAllowFocus(boolean allow);

  /**
   * Sets the visibility of main view. It will not receive input events when it is invisible,
   * regardless of what the focus allowance directs.
   *
   * @param visible If {@code true}, the view will be shown, if {@code false}, it will be hidden
   */
  void setVisible(boolean visible);
}
