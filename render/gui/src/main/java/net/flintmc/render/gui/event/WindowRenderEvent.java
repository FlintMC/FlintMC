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
import net.flintmc.render.gui.windowing.WindowRenderer;

/** Gets fired when a window renderer is being called (PRE and POST). */
@Subscribable({Phase.PRE, Phase.POST})
public class WindowRenderEvent extends DefaultGuiEvent implements GuiEvent {

  private final WindowRenderer windowRenderer;

  /**
   * Constructs a new {@link WindowRenderEvent} for the given window and renderer.
   *
   * @param window The non-null window to be rendered
   * @param windowRenderer The non-null renderer that renders the window
   */
  public WindowRenderEvent(Window window, WindowRenderer windowRenderer) {
    super(window);
    this.windowRenderer = windowRenderer;
  }

  /** @return the {@link WindowRenderer} that is about to render (or just rendered). */
  public WindowRenderer getWindowRenderer() {
    return this.windowRenderer;
  }
}
