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
import java.util.List;

/**
 * List of {@link Suggestion}s sent by the server for a tab completion request in the chat.
 *
 * @see ChatController#requestSuggestions(String)
 */
public interface SuggestionList {

  /**
   * The start index in the requested string where the suggestion should be inserted. For example if
   * the requested string is "/", the start index would be 1.
   * <p>
   * When the suggestion is inserted, everything from the start index until {@link #getEndIndex()}
   * should be removed.
   *
   * @return The start index sent by the server
   */
  int getStartIndex();

  /**
   * The end index of the requested string in the suggestions of this list. For example if the
   * requested string is "/a", the end index would be 2.
   * <p>
   * When the suggestion is inserted, everything from {@link #getStartIndex()} until the end index
   * should be removed.
   *
   * @return The end index sent by the server
   */
  int getEndIndex();

  /**
   * Retrieves all suggestions that are present in this list.
   *
   * @return The non-null immutable list of suggestions in this list
   */
  List<Suggestion> getSuggestions();

  /**
   * Factory for the {@link SuggestionList}.
   */
  @AssistedFactory(SuggestionList.class)
  interface Factory {

    /**
     * Creates a new {@link SuggestionList}.
     *
     * @param startIndex  The index where the suggestion should be inserted at
     * @param endIndex    The end index that represents the text that should be removed when the
     *                    suggestion is inserted
     * @param suggestions The non-null list of suggestions that were sent by the server
     * @return The new non-null {@link SuggestionList}
     */
    SuggestionList create(
        @Assisted("startIndex") int startIndex, @Assisted("endIndex") int endIndex,
        @Assisted("suggestions") List<Suggestion> suggestions);

  }

}
