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

package net.flintmc.mcapi.chat.component.event.content;

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;

/**
 * The content of a {@link HoverEvent} which displays a {@link ChatComponent}.
 */
public class HoverText extends HoverContent {

  private final ChatComponent text;

  /**
   * Creates a new content for a {@link HoverEvent} which displays a text.
   *
   * @param text The non-null component for the content
   */
  public HoverText(ChatComponent text) {
    this.text = text;
  }

  /**
   * Retrieves the non-null component of this text which is used when displaying the text.
   *
   * @return The non-null component of this text.
   */
  public ChatComponent getText() {
    return this.text;
  }

  @Override
  public HoverEvent.Action getAction() {
    return HoverEvent.Action.SHOW_TEXT;
  }
}
