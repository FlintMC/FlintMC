package net.labyfy.chat.serializer.gson.hover;

import com.google.gson.*;
import net.labyfy.chat.component.event.HoverEvent;
import net.labyfy.chat.component.event.content.HoverContent;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;

/**
 * The serializer for hover events in minecraft versions 1.16 and above.
 */
public class ModernHoverEventSerializer extends HoverEventSerializer {

  public ModernHoverEventSerializer(Logger logger) {
    super(logger);
  }

  @Override
  public HoverEvent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    if (!json.isJsonObject()) {
      return null;
    }

    JsonObject object = json.getAsJsonObject();

    HoverEvent.Action action = super.deserializeAction(object);
    if (action == null) {
      return null;
    }

    JsonElement value = object.get("contents");
    HoverContent[] contents;
    if (value.isJsonArray()) {
      contents = context.deserialize(value, action.getContentArrayClass());
    } else {
      contents = new HoverContent[]{context.deserialize(value, action.getContentClass())};
      if (contents[0] == null) {
        return null;
      }
    }

    return contents != null ? HoverEvent.of(contents) : null;
  }

  @Override
  public JsonElement serialize(HoverEvent src, Type typeOfSrc, JsonSerializationContext context) {
    if (src.getContents().length == 0) {
      return null;
    }

    JsonObject object = new JsonObject();

    object.addProperty("action", src.getContents()[0].getAction().getLowerName());
    object.add("contents", context.serialize(src.getContents()));

    return object;
  }
}
