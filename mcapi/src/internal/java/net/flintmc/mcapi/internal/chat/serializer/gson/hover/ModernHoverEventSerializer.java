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

import com.google.gson.JsonArray;
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
 * The serializer for hover events in minecraft versions 1.16 and above.
 */
public class ModernHoverEventSerializer extends HoverEventSerializer {

  public ModernHoverEventSerializer(
      Logger logger,
      GsonComponentSerializer componentSerializer,
      ComponentBuilder.Factory componentFactory) {
    super(logger, componentSerializer, componentFactory);
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

    JsonElement value = object.get("contents");
    HoverContent[] contents;
    if (value.isJsonArray()) {
      JsonArray array = value.getAsJsonArray();
      contents = new HoverContent[array.size()];

      for (int i = 0; i < contents.length; i++) {
        contents[i] = this.deserialize(serializer, array.get(i), context);
      }

    } else {
      contents = new HoverContent[]{this.deserialize(serializer, value, context)};
      if (contents[0] == null) {
        return null;
      }
    }

    return HoverEvent.of(contents);
  }

  private HoverContent deserialize(
      HoverContentSerializer serializer, JsonElement element, JsonDeserializationContext context) {
    return serializer.deserialize(
        super.asComponent(element, context),
        this.componentFactory,
        super.componentSerializer.getGson());
  }

  @Override
  public JsonElement serialize(HoverEvent src, Type typeOfSrc, JsonSerializationContext context) {
    if (src.getContents().length == 0) {
      return null;
    }

    HoverEvent.Action action = src.getContents()[0].getAction();

    JsonObject object = new JsonObject();

    object.addProperty("action", action.getLowerName());

    HoverContentSerializer serializer = super.componentSerializer.getHoverContentSerializer(action);

    JsonArray array = new JsonArray();
    for (HoverContent content : src.getContents()) {
      ChatComponent component =
          serializer.serialize(content, this.componentFactory, super.componentSerializer.getGson());
      if (component != null) {
        array.add(context.serialize(component));
      }
    }
    object.add("contents", array);

    return object;
  }
}
