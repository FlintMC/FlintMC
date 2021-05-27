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

package net.flintmc.mcapi.internal.chat.serializer.gson.hover.content;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.chat.component.event.content.HoverItem;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.ItemStackSerializer;

/**
 * Serializer for {@link HoverItem}
 */
@Singleton
public class HoverItemSerializer extends JsonHoverContentSerializer {

  private final ItemStackSerializer itemStackSerializer;
  private final HoverContent.Factory contentFactory;

  @Inject
  private HoverItemSerializer(
      ItemStackSerializer itemStackSerializer, HoverContent.Factory contentFactory) {
    this.contentFactory = contentFactory;
    this.itemStackSerializer = itemStackSerializer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected HoverContent deserializeJson(
      JsonElement element, ComponentBuilder.Factory componentFactory, Gson gson)
      throws JsonParseException {
    if (!element.isJsonObject()) {
      return null;
    }
    ItemStack item = this.itemStackSerializer.fromJson(element.getAsJsonObject());
    return item != null ? this.contentFactory.item(item) : null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected JsonElement serializeJson(
      HoverContent content, ComponentBuilder.Factory componentFactory, Gson gson)
      throws JsonParseException {
    HoverItem item = (HoverItem) content;
    return this.itemStackSerializer.toJson(item.getItemStack());
  }
}
