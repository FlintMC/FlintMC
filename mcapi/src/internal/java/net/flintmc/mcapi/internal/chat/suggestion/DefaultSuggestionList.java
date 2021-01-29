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

package net.flintmc.mcapi.internal.chat.suggestion;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.suggestion.Suggestion;
import net.flintmc.mcapi.chat.suggestion.SuggestionList;

import java.util.Collections;
import java.util.List;

@Implement(SuggestionList.class)
public class DefaultSuggestionList implements SuggestionList {

  private final int startIndex;
  private final int endIndex;
  private final List<Suggestion> suggestions;

  @AssistedInject
  public DefaultSuggestionList(
      @Assisted("startIndex") int startIndex, @Assisted("endIndex") int endIndex,
      @Assisted("suggestions") List<Suggestion> suggestions) {
    this.startIndex = startIndex;
    this.endIndex = endIndex;
    this.suggestions = Collections.unmodifiableList(suggestions);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getStartIndex() {
    return this.startIndex;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getEndIndex() {
    return this.endIndex;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Suggestion> getSuggestions() {
    return this.suggestions;
  }
}
