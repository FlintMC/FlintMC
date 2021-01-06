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

package net.flintmc.mcapi.internal.chat.serializer.gson.hover;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.component.event.content.HoverContentSerializer;
import net.flintmc.mcapi.chat.serializer.GsonComponentSerializer;
import net.flintmc.mcapi.internal.chat.builder.DefaultTextComponentBuilder;
import org.apache.logging.log4j.Logger;

/**
 * The serializer for HoverEvents in any Minecraft version.
 */
public abstract class HoverEventSerializer
    implements JsonSerializer<HoverEvent>, JsonDeserializer<HoverEvent> {

  protected final GsonComponentSerializer componentSerializer;
  private final Logger logger;

  public HoverEventSerializer(Logger logger, GsonComponentSerializer componentSerializer) {
    this.logger = logger;
    this.componentSerializer = componentSerializer;
  }

  protected HoverEvent.Action deserializeAction(JsonObject object) {
    String actionName = object.get("action").getAsString().toUpperCase();

    try {
      return HoverEvent.Action.valueOf(actionName);
    } catch (IllegalArgumentException exception) {
      this.logger.trace("Invalid hover action", exception);
      return null;
    }
  }

  protected HoverContentSerializer parseSerializer(JsonObject object) {
    HoverEvent.Action action = this.deserializeAction(object);
    if (action == null) {
      return null;
    }

    return this.componentSerializer.getHoverContentSerializer(action);
  }

  protected ChatComponent asComponent(JsonElement value, JsonDeserializationContext context) {
    // this can be a plain text or a json component
    if (value.isJsonObject()) {
      return context.deserialize(value, ChatComponent.class);
    } else if (value.isJsonPrimitive()) {
      return new DefaultTextComponentBuilder().text(value.getAsString()).build();
    }

    return null;
  }
}
