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

package net.flintmc.mcapi.event;

import javax.annotation.Nullable;
import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.player.overlay.TabOverlay;

/**
 * This event will be fired when the tab header/footer is updated or reset (e.g. via {@link
 * TabOverlay#updateHeader(ChatComponent)} or {@link TabOverlay#updateFooter(ChatComponent)}). It
 * will be fired on both {@link Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phases, but
 * cancellation will only have an effect in the {@link Subscribe.Phase#PRE} phase.
 *
 * @see Subscribe
 */
public interface TabHeaderFooterUpdateEvent extends Event, Cancellable {

  /**
   * Retrieves the new header/footer that has been set to be displayed.
   *
   * @return The new header/footer component or {@code null} if it has been reset
   */
  ChatComponent getNewValue();

  /**
   * Retrieves the type of the component that has been updated.
   *
   * @return The non-null type of the component that has been updated
   * @see Type
   */
  Type getType();

  /**
   * An enumeration of types of components that can be updated in the {@link
   * TabHeaderFooterUpdateEvent}.
   */
  enum Type {
    /**
     * The header in the tab list has been updated.
     */
    HEADER,
    /**
     * The footer in the tab list has been updated.
     */
    FOOTER
  }

  /**
   * Factory for the {@link TabHeaderFooterUpdateEvent}.
   */
  @AssistedFactory(TabHeaderFooterUpdateEvent.class)
  interface Factory {

    /**
     * Creates a new {@link TabHeaderFooterUpdateEvent}.
     *
     * @param newValue The component that has been updated or {@code null} if it has been reset
     * @param type     The non-null type of the component that has been updated
     * @return The new non-null {@link TabHeaderFooterUpdateEvent}.
     */
    TabHeaderFooterUpdateEvent create(
        @Assisted("new") @Nullable ChatComponent newValue, @Assisted Type type);
  }
}
