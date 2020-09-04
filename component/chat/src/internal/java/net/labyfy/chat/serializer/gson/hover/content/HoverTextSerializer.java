package net.labyfy.chat.serializer.gson.hover.content;

import com.google.gson.*;
import net.labyfy.chat.builder.DefaultTextComponentBuilder;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.component.event.content.HoverText;

import java.lang.reflect.Type;

public class HoverTextSerializer implements JsonSerializer<HoverText>, JsonDeserializer<HoverText> {
  @Override
  public HoverText deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    ChatComponent component = null;

    // this can be a plain text or a json component
    if (json.isJsonObject()) {
      component = context.deserialize(json, ChatComponent.class);
    } else if (json.isJsonPrimitive()) {
      component = new DefaultTextComponentBuilder().text(json.getAsString()).build();
    }

    if (component == null) {
      return null;
    }

    return new HoverText(component);
  }

  @Override
  public JsonElement serialize(HoverText src, Type typeOfSrc, JsonSerializationContext context) {
    return context.serialize(src.getText());
  }
}
