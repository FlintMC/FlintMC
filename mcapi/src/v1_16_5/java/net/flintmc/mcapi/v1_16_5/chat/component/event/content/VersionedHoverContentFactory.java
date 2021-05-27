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

package net.flintmc.mcapi.v1_16_5.chat.component.event.content;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.UUID;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.items.ItemStack;

@Singleton
@Implement(HoverContent.Factory.class)
public class VersionedHoverContentFactory implements HoverContent.Factory {

  private final VersionedHoverText.VersionedFactory textFactory;
  private final VersionedHoverEntity.VersionedFactory entityFactory;
  private final VersionedHoverItem.VersionedFactory itemFactory;

  @Inject
  private VersionedHoverContentFactory(
      VersionedHoverText.VersionedFactory textFactory,
      VersionedHoverEntity.VersionedFactory entityFactory,
      VersionedHoverItem.VersionedFactory itemFactory) {
    this.textFactory = textFactory;
    this.entityFactory = entityFactory;
    this.itemFactory = itemFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HoverContent text(ChatComponent component) {
    return this.textFactory.create(component);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HoverContent entity(UUID uniqueId, String type) {
    return this.entityFactory.create(type, uniqueId, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HoverContent entity(UUID uniqueId, String type, ChatComponent displayName) {
    return this.entityFactory.create(type, uniqueId, displayName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HoverContent item(ItemStack itemStack) {
    return this.itemFactory.create(itemStack);
  }
}
