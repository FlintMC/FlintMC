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

import java.util.UUID;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;

/**
 * The content of a {@link HoverEvent}.
 */
public abstract class HoverContent {

  /**
   * Creates a new content for a {@link HoverEvent} which displays a text.
   *
   * @param component The non-null component for the content
   * @return The new non-null content to be used in a {@link HoverEvent}
   */
  public static HoverContent text(ChatComponent component) {
    return new HoverText(component);
  }

  /**
   * Creates a new content for a {@link HoverEvent} which displays an entity.
   *
   * @param uniqueId The non-null uniqueId of the entity
   * @param type     The non-null type of the entity
   * @return The new non-null content to be used in a {@link HoverEvent}
   */
  public static HoverContent entity(UUID uniqueId, String type) {
    return entity(uniqueId, type, null);
  }

  /**
   * Creates a new content for a {@link HoverEvent} which displays an entity.
   *
   * @param uniqueId    The non-null uniqueId of the entity
   * @param type        The non-null type of the entity
   * @param displayName The display name of the entity or {@code null}, if the entity doesn't have a
   *                    specific display name
   * @return The new non-null content to be used in a {@link HoverEvent}
   */
  public static HoverContent entity(UUID uniqueId, String type, ChatComponent displayName) {
    return new HoverEntity(uniqueId, type, displayName);
  }

  /**
   * Retrieves the action which is used to identify the type of this content.
   *
   * @return The non-null action of this content
   */
  public abstract HoverEvent.Action getAction();
}
