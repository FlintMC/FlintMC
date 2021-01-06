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
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.UUID;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.chat.component.event.content.HoverEntity;
import net.flintmc.mcapi.chat.component.event.content.JsonHoverContentSerializer;

/**
 * Serializer for {@link HoverEntity}
 */
public class HoverEntitySerializer extends JsonHoverContentSerializer {

  @Override
  protected HoverContent deserializeJson(
      JsonElement element, ComponentBuilder.Factory componentFactory, Gson gson)
      throws JsonParseException {
    if (!element.isJsonObject()) {
      return null;
    }

    JsonObject object = element.getAsJsonObject();
    if (!object.has("type")) {
      return null;
    }

    // non-null uuid of the entity
    UUID uniqueId = gson.fromJson(object.get("id"), UUID.class);
    if (uniqueId == null) {
      return null;
    }

    return new HoverEntity(
        uniqueId,
        object.get("type").getAsString(), // non-null type of the entity
        object.has("name")
            ? gson.fromJson(object.get("name"), ChatComponent.class)
            : null // nullable display name of the entity
    );
  }

  @Override
  protected JsonElement serializeJson(
      HoverContent content, ComponentBuilder.Factory componentFactory, Gson gson)
      throws JsonParseException {
    HoverEntity entity = (HoverEntity) content;

    JsonObject object = new JsonObject();

    object.addProperty("type", entity.getType());
    object.addProperty("id", entity.getUniqueId().toString());
    if (entity.getDisplayName() != null) {
      object.add("name", gson.toJsonTree(entity.getDisplayName()));
    }

    return object;
  }
}
