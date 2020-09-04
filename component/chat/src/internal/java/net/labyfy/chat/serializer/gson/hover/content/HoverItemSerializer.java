package net.labyfy.chat.serializer.gson.hover.content;

import com.google.gson.*;
import net.labyfy.chat.component.event.content.HoverItem;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;

public class HoverItemSerializer implements JsonSerializer<HoverItem>, JsonDeserializer<HoverItem> {

  private final Logger logger;

  public HoverItemSerializer(Logger logger) {
    this.logger = logger;
  }

  @Override
  public HoverItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    if (!json.isJsonObject()) {
      return null;
    }
    JsonObject object = json.getAsJsonObject();
    if (!object.has("id")) {
      return null;
    }

    int count = this.parseCount(object.get("Count"));

    return new HoverItem(object.get("id").getAsString(), count, object.has("tag") ? object.get("tag").toString() : null);
  }

  @Override
  public JsonElement serialize(HoverItem src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject object = new JsonObject();
    object.addProperty("id", src.getId());
    if (src.getCount() != 1) {
      object.addProperty("Count", src.getCount());
    }

    if (src.getNbt() != null) {
      object.add("tag", JsonParser.parseString(src.getNbt()));
    }

    return object;
  }

  private int parseCount(JsonElement element) {
    if (element != null && element.isJsonPrimitive()) {

      JsonPrimitive count = element.getAsJsonPrimitive();

      if (count.isNumber()) {
        return count.getAsInt();
      } else if (count.isString()) {
        String countString = count.getAsString();
        if (!countString.isEmpty()) {
          char c = countString.charAt(countString.length() - 1);

          // check if the last char is not a number
          // The text might end with a suffix for the number like 's' for short, 'l' for long
          if (c > 9) {
            countString = countString.substring(0, countString.length() - 1);
          }

          try {
            return Integer.parseInt(countString);
          } catch (NumberFormatException exception) {
            this.logger.trace("Invalid count for item in an HoverEvent", exception);
          }

        }
      }
    }

    return 1;
  }

}
