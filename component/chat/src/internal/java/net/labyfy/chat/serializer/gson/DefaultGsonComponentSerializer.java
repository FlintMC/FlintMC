package net.labyfy.chat.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.component.event.HoverEvent;
import net.labyfy.chat.component.event.content.HoverEntity;
import net.labyfy.chat.component.event.content.HoverItem;
import net.labyfy.chat.component.event.content.HoverText;
import net.labyfy.chat.exception.ComponentDeserializationException;
import net.labyfy.chat.serializer.ComponentSerializer;
import net.labyfy.chat.serializer.GsonComponentSerializer;
import net.labyfy.chat.serializer.gson.hover.LegacyHoverEventSerializer;
import net.labyfy.chat.serializer.gson.hover.ModernHoverEventSerializer;
import net.labyfy.chat.serializer.gson.hover.content.HoverEntitySerializer;
import net.labyfy.chat.serializer.gson.hover.content.HoverItemSerializer;
import net.labyfy.chat.serializer.gson.hover.content.HoverTextSerializer;
import org.apache.logging.log4j.Logger;

public class DefaultGsonComponentSerializer implements GsonComponentSerializer {

  private final Gson gson;

  public DefaultGsonComponentSerializer(Logger logger, ComponentSerializer.Factory factory, boolean legacyHover) {
    this.gson = new GsonBuilder()
        .registerTypeHierarchyAdapter(ChatComponent.class, new GsonChatComponentSerializer(logger))
        .registerTypeAdapter(HoverEvent.class, legacyHover ? new LegacyHoverEventSerializer(factory, logger) : new ModernHoverEventSerializer(logger))
        .registerTypeAdapter(HoverEntity.class, new HoverEntitySerializer())
        .registerTypeAdapter(HoverItem.class, new HoverItemSerializer(logger))
        .registerTypeAdapter(HoverText.class, new HoverTextSerializer())
        .create();
  }

  @Override
  public String serialize(ChatComponent component) {
    return this.gson.toJson(component);
  }

  @Override
  public ChatComponent deserialize(String serialized) {
    try {
      return this.gson.fromJson(serialized, ChatComponent.class);
    } catch (JsonSyntaxException exception) {
      throw new ComponentDeserializationException("Invalid json", exception);
    }
  }

  @Override
  public Gson getGson() {
    return this.gson;
  }
}
