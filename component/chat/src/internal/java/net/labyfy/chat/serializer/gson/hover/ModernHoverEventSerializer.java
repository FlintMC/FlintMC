package net.labyfy.chat.serializer.gson.hover;

import com.google.gson.*;
import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.component.event.HoverEvent;
import net.labyfy.chat.component.event.content.HoverContent;
import net.labyfy.chat.component.event.content.HoverContentSerializer;
import net.labyfy.chat.serializer.GsonComponentSerializer;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;

/**
 * The serializer for hover events in minecraft versions 1.16 and above.
 */
public class ModernHoverEventSerializer extends HoverEventSerializer {

  private final ComponentBuilder.Factory componentFactory;

  public ModernHoverEventSerializer(Logger logger, GsonComponentSerializer componentSerializer, ComponentBuilder.Factory componentFactory) {
    super(logger, componentSerializer);
    this.componentFactory = componentFactory;
  }

  @Override
  public HoverEvent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
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

  private HoverContent deserialize(HoverContentSerializer serializer, JsonElement element, JsonDeserializationContext context) {
    return serializer.deserialize(super.asComponent(element, context), this.componentFactory, super.componentSerializer.getGson());
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
      ChatComponent component = serializer.serialize(content, this.componentFactory, super.componentSerializer.getGson());
      if (component != null) {
        array.add(context.serialize(component));
      }
    }
    object.add("contents", array);

    return object;
  }
}
