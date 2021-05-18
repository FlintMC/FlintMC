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

package net.flintmc.mcapi.server.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.server.ServerController;

/**
 * This event will be fired multiple times whenever the user connects to a server. It doesn't matter
 * whether the connection has been called through the {@link ServerController} or by the user. It
 * will only be fired in the PRE phase.
 * <p>
 * For offline mode servers, this event will only be fired once with the {@link Stage#JOINING}
 * stage. For online mode servers, it will be fired once each in the {@link Stage#AUTHORIZING},
 * {@link Stage#ENCRYPTING} and {@link Stage#JOINING} stages.
 * <p>
 * It may also be fired with the {@link Stage#NEGOTIATING} stage when the server sends a custom
 * payload in the login process.
 *
 * @see Stage
 * @see Subscribe
 */
@Subscribable(Phase.PRE)
public interface ServerConnectProgressEvent extends Event {

  /**
   * Retrieves the message that is shown in the client representing the current {@link
   * #getConnectingStage() stage}.
   *
   * @return The non-null message shown in the client representing the current stage
   */
  ChatComponent getStatusMessage();

  /**
   * Retrieves the current stage of the login process.
   *
   * @return The non-null stage of the login process
   */
  Stage getConnectingStage();

  /**
   * Stages in the login process to a server.
   */
  enum Stage {

    /**
     * This stage will be fired when the client begins to authorize with the server against Mojang.
     */
    AUTHORIZING,

    /**
     * This stage will be fired after the client is done with {@link #AUTHORIZING} and begins with
     * enabling encryption in the connection to the server.
     */
    ENCRYPTING,

    /**
     * This stage will be fired after the {@link #AUTHORIZING} and {@link #ENCRYPTING} stages are
     * finished (or for offline mode servers directly after connecting).
     */
    JOINING,

    /**
     * This stage will be fired before the {@link #JOINING} stage has been fired and only if the
     * server sends a custom payload in the login process, otherwise it will never be fired.
     */
    NEGOTIATING,

    /**
     * This stage will be fired if the client switched to a stage that is unknown to Flint which
     * should basically never happen.
     */
    UNKNOWN
  }

  /**
   * Factory for the {@link ServerConnectProgressEvent}.
   */
  @AssistedFactory(ServerConnectProgressEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerConnectProgressEvent} with the given status message and stage.
     *
     * @param statusMessage   The non-null message shown in the client representing the current
     *                        stage
     * @param connectingStage The non-null stage of the login process
     * @return The new event
     */
    ServerConnectProgressEvent create(
        @Assisted("statusMessage") ChatComponent statusMessage,
        @Assisted("connectingStage") Stage connectingStage);
  }
}
