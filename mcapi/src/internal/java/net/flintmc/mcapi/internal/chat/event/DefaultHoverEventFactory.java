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

package net.flintmc.mcapi.internal.chat.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.UUID;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent.Factory;

@Singleton
@Implement(HoverEvent.Factory.class)
public class DefaultHoverEventFactory implements HoverEvent.Factory {

  private final HoverContent.Factory contentFactory;

  @Inject
  private DefaultHoverEventFactory(Factory contentFactory) {
    this.contentFactory = contentFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HoverEvent create(HoverContent... contents) {
    return new DefaultHoverEvent(contents);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HoverEvent text(ChatComponent... texts) {
    HoverContent[] contents = new HoverContent[texts.length];
    for (int i = 0; i < texts.length; i++) {
      contents[i] = this.contentFactory.text(texts[i]);
    }
    return this.create(contents);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HoverEvent entity(UUID entityId, String type) {
    return this.create(this.contentFactory.entity(entityId, type));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HoverEvent entity(UUID entityId, String type, ChatComponent displayName) {
    return this.create(this.contentFactory.entity(entityId, type, displayName));
  }
}
