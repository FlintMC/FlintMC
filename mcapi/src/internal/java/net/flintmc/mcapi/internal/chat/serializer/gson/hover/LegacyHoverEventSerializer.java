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

import com.google.common.base.Preconditions;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.chat.component.event.content.HoverContentSerializer;
import net.flintmc.mcapi.chat.serializer.GsonComponentSerializer;
import org.apache.logging.log4j.Logger;

/**
 * The serializer for HoverEvents in minecraft versions below 1.16.
 */
public class LegacyHoverEventSerializer extends HoverEventSerializer {

  private final ComponentBuilder.Factory componentFactory;

  public LegacyHoverEventSerializer(
      Logger logger,
      ComponentBuilder.Factory componentFactory,
      GsonComponentSerializer componentSerializer) {
    super(logger, componentSerializer);
    this.componentFactory = componentFactory;
  }

  @Override
  public HoverEvent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    if (!json.isJsonObject()) {
      return null;
    }

    JsonObject object = json.getAsJsonObject();
    HoverContentSerializer serializer = super.parseSerializer(object);
    if (serializer == null) {
      return null;
    }

    ChatComponent component = super.asComponent(object.get("value"), context);
    if (component == null) {
      return null;
    }

    HoverContent content =
        serializer.deserialize(
            component, this.componentFactory, super.componentSerializer.getGson());
    if (content == null) {
      return null;
    }
    return HoverEvent.of(content);
  }

  @Override
  public JsonElement serialize(HoverEvent src, Type typeOfSrc, JsonSerializationContext context) {
    Preconditions.checkArgument(
        src.getContents().length == 1, "Legacy hover events cannot have multiple contents");

    HoverContent content = src.getContents()[0];
    HoverEvent.Action action = content.getAction();

    JsonObject object = new JsonObject();
    object.addProperty("action", action.getLowerName());

    ChatComponent component = super.componentSerializer.serializeHoverContent(content);
    if (component == null) {
      return null;
    }

    object.add("value", context.serialize(component));

    return object;
  }
}
