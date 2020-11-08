package net.flintmc.mcapi.internal.chat.serializer.gson.hover;

import com.google.common.base.Preconditions;
import com.google.gson.*;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.chat.component.event.content.HoverContentSerializer;
import net.flintmc.mcapi.chat.serializer.GsonComponentSerializer;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;

/** The serializer for HoverEvents in minecraft versions below 1.16. */
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
