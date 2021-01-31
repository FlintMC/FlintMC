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

package net.flintmc.mcapi.chat.suggestion;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.ChatController;
import net.flintmc.mcapi.chat.component.ChatComponent;

import javax.annotation.Nullable;

/**
 * Represents a suggestion of a tab completion request in the chat.
 *
 * @see ChatController#requestSuggestions(String)
 */
public interface Suggestion {

  /**
   * Retrieves the text sent by the server for this suggestion.
   *
   * @return The non-null text sent by the server
   */
  String getText();

  /**
   * Retrieves the tooltip sent by the server that should be read out when the Narrator is enabled.
   *
   * @return The tooltip or {@code null} if it shouldn't say any more details
   */
  ChatComponent getTooltip();

  /**
   * Factory for the {@link Suggestion}.
   */
  @AssistedFactory(Suggestion.class)
  interface Factory {

    /**
     * Creates a new {@link Suggestion} with the text and optional tooltip.
     *
     * @param text    The non-null text sent by the server
     * @param tooltip The tooltip to be read out by the Narrator or {@code null} if it shouldn't say
     *                any more details
     * @return The new non-null {@link Suggestion}
     */
    Suggestion create(
        @Assisted("text") String text, @Assisted("tooltip") @Nullable ChatComponent tooltip);

  }

}
