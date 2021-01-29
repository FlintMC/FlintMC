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
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.suggestion.Suggestion;
import javax.annotation.Nullable;

@Implement(Suggestion.class)
public class DefaultSuggestion implements Suggestion {

  private final String text;
  private final ChatComponent tooltip;

  @AssistedInject
  public DefaultSuggestion(
      @Assisted("text") String text, @Assisted("tooltip") @Nullable ChatComponent tooltip) {
    this.text = text;
    this.tooltip = tooltip;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getText() {
    return this.text;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getTooltip() {
    return this.tooltip;
  }
}
