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

import net.flintmc.render.gui.windowing.Window;

/**
 * Base class for {@link GuiEvent}s which means cancellable events with a window.
 */
public class DefaultGuiEvent implements GuiEvent {

  private final Window window;
  private boolean cancelled;

  /**
   * Constructs a new {@link DefaultGuiEvent} for the given window.
   *
   * @param window The non-null window where this event has happened
   */
  protected DefaultGuiEvent(Window window) {
    this.window = window;
  }

  @Override
  public Window getWindow() {
    return this.window;
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
