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

package net.flintmc.mcapi.chat.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.generation.annotation.DataFactory;
import net.flintmc.framework.generation.annotation.TargetDataField;
import net.flintmc.mcapi.chat.ChatController;

/**
 * This event will be fired whenever the content of the chat input line has been changed through a
 * click event or any other way to manually set the input like {@link
 * ChatController#setInputValue(String, boolean)}, it will never be fired just because the user
 * types something into the chat.
 * <p>
 * It will only be fired in the {@link Phase#POST} phase.
 *
 * @see Subscribe
 */
@Subscribable(Phase.POST)
public interface ChatInputChangeEvent extends Event {

  /**
   * Retrieves the text to which the chat input has been changed.
   *
   * @return The non-null text to which the chat input has been changed
   */
  @TargetDataField("newValue")
  String getNewValue();

  /**
   * Factory for the {@link ChatInputChangeEvent}.
   */
  @DataFactory(ChatInputChangeEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ChatInputChangeEvent} for the given new value.
     *
     * @param newValue The non-null text to which the chat input has been changed
     * @return The new non-null event
     */
    ChatInputChangeEvent create(@TargetDataField("newValue") String newValue);
  }

}
