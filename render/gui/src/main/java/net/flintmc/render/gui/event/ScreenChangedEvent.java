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
import net.flintmc.render.gui.screen.ScreenName;
import net.flintmc.render.gui.windowing.Window;

/**
 * Event indicating that the GUI screen has changed on a window.
 *
 * <p>This event is only fired on the main window by default.
 */
@Subscribable(Phase.PRE)
public class ScreenChangedEvent extends DefaultGuiEvent implements GuiEvent {
  private final ScreenName screenName;

  /**
   * Constructs a new {@link ScreenChangedEvent} with the given screen name.
   *
   * @param window The non-null window where this event has happened
   * @param screenName The name of the new GUI screen, or {@code null}, if no screen is displayed
   *     anymore
   */
  public ScreenChangedEvent(Window window, ScreenName screenName) {
    super(window);
    this.screenName = screenName;
  }

  /**
   * Retrieves the newly displayed screen name.
   *
   * @return The name of the newly displayed screen, or {@code null}, if no screen is displayed
   *     anymore
   */
  public ScreenName getScreenName() {
    return screenName;
  }
}
