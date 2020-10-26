package net.labyfy.chat.serializer.gson.hover.content;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.component.event.content.HoverContent;
import net.labyfy.chat.component.event.content.HoverEntity;
import net.labyfy.chat.component.event.content.JsonHoverContentSerializer;

import java.util.UUID;

/**
 * Serializer for {@link HoverEntity}
 */
public class HoverEntitySerializer extends JsonHoverContentSerializer {
  @Override
  protected HoverContent deserializeJson(JsonElement element, ComponentBuilder.Factory componentFactory, Gson gson) throws JsonParseException {
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
        object.has("name") ? gson.fromJson(object.get("name"), ChatComponent.class) : null // nullable display name of the entity
    );
  }

  @Override
  protected JsonElement serializeJson(HoverContent content, ComponentBuilder.Factory componentFactory, Gson gson) throws JsonParseException {
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
