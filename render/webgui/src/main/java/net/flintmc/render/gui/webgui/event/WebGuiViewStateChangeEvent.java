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

package net.flintmc.render.gui.webgui.event;

import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.render.gui.webgui.WebGuiView;

/**
 * Event indicating that a {@link WebGuiView} state has changed.
 */
@Subscribable({Phase.PRE, Phase.POST})
public interface WebGuiViewStateChangeEvent extends WebGuiViewEvent {

  /**
   * Retrieves the ID of the frame that sent the event.
   *
   * @return The ID of the source frame
   */
  long frameId();

  /**
   * Retrieves the URL of the frame that sent the event.
   *
   * @return The url of the frame
   */
  String url();

  /**
   * Determines whether the event comes from the main frame or some sub frame that sent the event.
   *
   * @return {@code true} if the event has been fired by the main frame, {@code false} otherwise
   */
  boolean isMainFrame();
}
