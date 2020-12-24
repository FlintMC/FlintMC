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
 * The content of a {@link HoverEvent} which displays an entity.
 */
public class HoverEntity extends HoverContent {

  private final UUID uniqueId;
  private final String type;
  private final ChatComponent displayName;

  /**
   * Creates a new content for a {@link HoverEvent} which displays an entity.
   *
   * @param uniqueId    The non-null uniqueId of the entity
   * @param type        The non-null type of the entity
   * @param displayName The display name of the entity or {@code null} if the entity doesn't have a
   *                    specific display name
   */
  public HoverEntity(UUID uniqueId, String type, ChatComponent displayName) {
    this.uniqueId = uniqueId;
    this.type = type;
    this.displayName = displayName;
  }

  /**
   * Retrieves the non-null type of this entity which is used when displaying the entity.
   *
   * @return The non-null type of this entity
   */
  public String getType() {
    return this.type;
  }

  /**
   * Retrieves the non-null uniqueId of this entity which is used when displaying the entity.
   *
   * @return The non-null uniqueId of this entity
   */
  public UUID getUniqueId() {
    return this.uniqueId;
  }

  /**
   * Retrieves the display name of this entity which is used when displaying the entity.
   *
   * @return The display name of this entity or {@code null} if no display name has been set
   */
  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  @Override
  public HoverEvent.Action getAction() {
    return HoverEvent.Action.SHOW_ENTITY;
  }
}
