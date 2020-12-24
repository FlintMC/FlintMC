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

package net.flintmc.util.session.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.util.session.SessionService;

/**
 * This event will be fired in the POST state whenever the {@link SessionService} has refreshed its
 * token.
 *
 * @see SessionService#refreshToken()
 */
public interface SessionTokenRefreshEvent extends Event {

  /**
   * Retrieves the access token that has been used before the token has been refreshed.
   *
   * @return The non-null access token before it has been refreshed
   */
  String getPreviousAccessToken();

  /**
   * Retrieves the new access token from the refresh.
   *
   * @return The non-null access token after it has been refreshed
   */
  String getNewAccessToken();

  /**
   * Factory for the {@link SessionTokenRefreshEvent}.
   */
  @AssistedFactory(SessionTokenRefreshEvent.class)
  interface Factory {

    /**
     * Creates a new {@link SessionTokenRefreshEvent} with the given tokens.
     *
     * @param previousToken The non-null token from before it has been refreshed (this is always
     *                      given because the token cannot be refreshed without having a token)
     * @param newToken      The non-null token after it has been refreshed
     * @return The new non-null {@link SessionTokenRefreshEvent}
     */
    SessionTokenRefreshEvent create(
        @Assisted("previousToken") String previousToken, @Assisted("newToken") String newToken);
  }
}
