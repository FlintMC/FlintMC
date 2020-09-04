package net.labyfy.chat.serializer.gson.hover.content;

import com.google.gson.*;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.component.event.content.HoverEntity;

import java.lang.reflect.Type;
import java.util.UUID;

public class HoverEntitySerializer implements JsonSerializer<HoverEntity>, JsonDeserializer<HoverEntity> {
  @Override
  public HoverEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    if (!json.isJsonObject()) {
      return null;
    }
    JsonObject object = json.getAsJsonObject();
    if (!object.has("type")) {
      return null;
    }

    // non-null uuid of the entity
    UUID uniqueId = context.deserialize(object.get("id"), UUID.class);
    if (uniqueId == null) {
      return null;
    }

    return new HoverEntity(
        uniqueId,
        object.get("type").getAsString(), // non-null type of the entity
        object.has("name") ? context.deserialize(object.get("name"), ChatComponent.class) : null // nullable display name of the entity
    );
  }

  @Override
  public JsonElement serialize(HoverEntity src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject object = new JsonObject();

    object.addProperty("type", src.getType());
    object.addProperty("id", src.getUniqueId().toString());
    if (src.getDisplayName() != null) {
      object.add("name", context.serialize(src.getDisplayName()));
    }

    return object;
  }
}
