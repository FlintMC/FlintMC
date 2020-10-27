package net.flintmc.mcapi.chat.internal.serializer.gson.hover;

import com.google.gson.*;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.internal.builder.DefaultTextComponentBuilder;
import net.flintmc.mcapi.chat.serializer.GsonComponentSerializer;
import net.flintmc.mcapi.chat.component.event.content.HoverContentSerializer;
import org.apache.logging.log4j.Logger;

/**
 * The serializer for HoverEvents in any Minecraft version.
 */
public abstract class HoverEventSerializer implements JsonSerializer<HoverEvent>, JsonDeserializer<HoverEvent> {

  private final Logger logger;
  protected final GsonComponentSerializer componentSerializer;

  public HoverEventSerializer(Logger logger, GsonComponentSerializer componentSerializer) {
    this.logger = logger;
    this.componentSerializer = componentSerializer;
  }

  protected HoverEvent.Action deserializeAction(JsonObject object) {
    String actionName = object.get("action").getAsString().toUpperCase();

    try {
      return HoverEvent.Action.valueOf(actionName);
    } catch (IllegalArgumentException exception) {
      this.logger.trace("Invalid hover action", exception);
      return null;
    }
  }

  protected HoverContentSerializer parseSerializer(JsonObject object) {
    HoverEvent.Action action = this.deserializeAction(object);
    if (action == null) {
      return null;
    }

    return this.componentSerializer.getHoverContentSerializer(action);
  }

  protected ChatComponent asComponent(JsonElement value, JsonDeserializationContext context) {
    // this can be a plain text or a json component
    if (value.isJsonObject()) {
      return context.deserialize(value, ChatComponent.class);
    } else if (value.isJsonPrimitive()) {
      return new DefaultTextComponentBuilder().text(value.getAsString()).build();
    }

    return null;
  }

}
