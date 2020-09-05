package net.labyfy.chat.serializer.gson.hover;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import net.labyfy.chat.component.event.HoverEvent;
import org.apache.logging.log4j.Logger;

/**
 * The serializer for HoverEvents in any Minecraft version.
 */
public abstract class HoverEventSerializer implements JsonSerializer<HoverEvent>, JsonDeserializer<HoverEvent> {

  private final Logger logger;

  public HoverEventSerializer(Logger logger) {
    this.logger = logger;
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

}
