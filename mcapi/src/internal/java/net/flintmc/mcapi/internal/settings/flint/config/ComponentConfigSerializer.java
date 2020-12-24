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

package net.flintmc.mcapi.internal.settings.flint.config;

import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.serialization.ConfigSerializationHandler;
import net.flintmc.framework.config.serialization.ConfigSerializer;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.chat.serializer.GsonComponentSerializer;

@Singleton
@ConfigSerializer(ChatComponent.class)
public class ComponentConfigSerializer implements ConfigSerializationHandler<ChatComponent> {

  private final GsonComponentSerializer serializer;

  @Inject
  public ComponentConfigSerializer(ComponentSerializer.Factory serializerFactory) {
    this.serializer = serializerFactory.gson();
  }

  @Override
  public JsonElement serialize(ChatComponent chatComponent) {
    return this.serializer.getGson().toJsonTree(chatComponent);
  }

  @Override
  public ChatComponent deserialize(JsonElement element) {
    return this.serializer.getGson().fromJson(element, ChatComponent.class);
  }
}
