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

/** Event indicating that the loading status of a {@link WebGuiView} has changed. */
@Subscribable({Phase.PRE, Phase.POST})
public interface WebGuiViewLoadingEvent extends WebGuiViewStateChangeEvent {
  /**
   * Retrieves error info about the event.
   *
   * @return The current error info, or {@code null}, if no error occurred
   */
  ErrorInfo errorInfo();

  /**
   * Determines if there was an error.
   *
   * @return {@code true} if an error occurred while loading, {@code false} otherwise
   */
  default boolean hasError() {
    return errorInfo() != null;
  }

  /** Error information for this event. */
  class ErrorInfo {
    private final String message;
    private final String domain;
    private final int code;

    /**
     * Constructs a new {@link ErrorInfo}.
     *
     * @param message The error message
     * @param domain The domain the error originated from
     * @param code The error code (HTTP code for HTTP errors, anything else for other errors)
     */
    public ErrorInfo(String message, String domain, int code) {
      this.message = message;
      this.domain = domain;
      this.code = code;
    }

    /**
     * Retrieves the error message.
     *
     * @return The error message
     */
    public String getMessage() {
      return message;
    }

    /**
     * Retrieves the domain the error originated from.
     *
     * @return The domain the error originated from
     */
    public String getDomain() {
      return domain;
    }

    /**
     * Retrieves the error code. Might be an HTTP status code for HTTP errors, or anything else for
     * implementation specific errors.
     *
     * @return The error code
     */
    public int getCode() {
      return code;
    }
  }
}
