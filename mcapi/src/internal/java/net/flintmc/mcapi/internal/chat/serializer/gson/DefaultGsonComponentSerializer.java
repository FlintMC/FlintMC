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

package net.flintmc.mcapi.internal.chat.serializer.gson;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.chat.component.event.content.HoverContentSerializer;
import net.flintmc.mcapi.chat.exception.ComponentDeserializationException;
import net.flintmc.mcapi.chat.serializer.GsonComponentSerializer;
import net.flintmc.mcapi.internal.chat.serializer.gson.hover.LegacyHoverEventSerializer;
import net.flintmc.mcapi.internal.chat.serializer.gson.hover.ModernHoverEventSerializer;
import net.flintmc.mcapi.internal.chat.serializer.gson.hover.content.HoverEntitySerializer;
import net.flintmc.mcapi.internal.chat.serializer.gson.hover.content.HoverTextSerializer;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;

public class DefaultGsonComponentSerializer implements GsonComponentSerializer {

  private final Gson gson;
  private final ComponentBuilder.Factory componentFactory;

  private final Map<HoverEvent.Action, HoverContentSerializer> hoverContentSerializers =
      new HashMap<>();

  public DefaultGsonComponentSerializer(
      Logger logger,
      GsonChatComponentSerializer componentSerializer,
      ComponentBuilder.Factory componentFactory,
      boolean legacyHover) {
    this.componentFactory = componentFactory;

    this.gson =
        new GsonBuilder()
            .registerTypeHierarchyAdapter(ChatComponent.class, componentSerializer)
            .registerTypeAdapter(
                HoverEvent.class,
                legacyHover
                    ? new LegacyHoverEventSerializer(logger, componentFactory, this)
                    : new ModernHoverEventSerializer(logger, this, componentFactory))
            .create();

    this.registerHoverContentSerializer(HoverEvent.Action.SHOW_TEXT, new HoverTextSerializer());
    this.registerHoverContentSerializer(HoverEvent.Action.SHOW_ENTITY, new HoverEntitySerializer());
    this.registerHoverContentSerializer(
        HoverEvent.Action.SHOW_ACHIEVEMENT,
        this.getHoverContentSerializer(HoverEvent.Action.SHOW_TEXT));
  }

  @Override
  public String serialize(ChatComponent component) {
    return this.gson.toJson(component);
  }

  @Override
  public ChatComponent deserialize(String serialized) {
    try {
      return this.gson.fromJson(serialized, ChatComponent.class);
    } catch (JsonSyntaxException exception) {
      throw new ComponentDeserializationException("Invalid json", exception);
    }
  }

  @Override
  public Gson getGson() {
    return this.gson;
  }

  @Override
  public HoverContent deserializeHoverContent(ChatComponent component, HoverEvent.Action action) {
    return this.getHoverContentSerializer(action)
        .deserialize(component, this.componentFactory, this.gson);
  }

  @Override
  public ChatComponent serializeHoverContent(HoverContent content) {
    return this.getHoverContentSerializer(content.getAction())
        .serialize(content, this.componentFactory, this.gson);
  }

  @Override
  public void registerHoverContentSerializer(
      HoverEvent.Action action, HoverContentSerializer serializer) {
    Preconditions.checkArgument(
        !this.hoverContentSerializers.containsKey(action),
        "A serializer for the action %s is already registered",
        action);

    this.hoverContentSerializers.put(action, serializer);
  }

  @Override
  public HoverContentSerializer getHoverContentSerializer(HoverEvent.Action action) {
    HoverContentSerializer serializer = this.hoverContentSerializers.get(action);
    if (serializer == null) {
      throw new UnsupportedOperationException("No serializer for the action " + action + " found");
    }
    return serializer;
  }
}
