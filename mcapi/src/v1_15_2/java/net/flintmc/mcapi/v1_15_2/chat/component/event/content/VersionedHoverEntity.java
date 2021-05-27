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

package net.flintmc.mcapi.v1_15_2.chat.component.event.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.chat.component.event.HoverEvent.Action;
import net.flintmc.mcapi.chat.component.event.content.HoverEntity;

public class VersionedHoverEntity implements HoverEntity {

  private final TextComponent.Factory textComponentFactory;

  private final String type;
  private final UUID uniqueId;
  private final ChatComponent displayName;

  private List<ChatComponent> cachedText;

  @AssistedInject
  private VersionedHoverEntity(
      TextComponent.Factory textComponentFactory,
      @Assisted String type,
      @Assisted UUID uniqueId,
      @Assisted @Nullable ChatComponent displayName) {
    this.textComponentFactory = textComponentFactory;
    this.type = type;
    this.uniqueId = uniqueId;
    this.displayName = displayName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Action getAction() {
    return Action.SHOW_ENTITY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ChatComponent> getAsText() {
    if (this.cachedText != null) {
      return this.cachedText;
    }

    List<ChatComponent> components = new ArrayList<>();

    if (this.displayName != null) {
      components.add(this.displayName);
    }

    TextComponent typeComponent = this.textComponentFactory.create();
    typeComponent.text(this.type);
    components.add(typeComponent);

    TextComponent idComponent = this.textComponentFactory.create();
    idComponent.text(this.uniqueId.toString());
    components.add(idComponent);

    return this.cachedText = Collections.unmodifiableList(components);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getType() {
    return this.type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getUniqueId() {
    return this.uniqueId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  @AssistedFactory(VersionedHoverEntity.class)
  interface VersionedFactory {

    VersionedHoverEntity create(
        @Assisted String type,
        @Assisted UUID uniqueId,
        @Assisted @Nullable ChatComponent displayName);
  }
}
