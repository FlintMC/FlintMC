package net.labyfy.chat.serializer.gson;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.component.event.HoverEvent;
import net.labyfy.chat.component.event.content.HoverContent;
import net.labyfy.chat.component.event.content.HoverContentSerializer;
import net.labyfy.chat.exception.ComponentDeserializationException;
import net.labyfy.chat.serializer.GsonComponentSerializer;
import net.labyfy.chat.serializer.gson.hover.LegacyHoverEventSerializer;
import net.labyfy.chat.serializer.gson.hover.ModernHoverEventSerializer;
import net.labyfy.chat.serializer.gson.hover.content.HoverEntitySerializer;
import net.labyfy.chat.serializer.gson.hover.content.HoverTextSerializer;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class DefaultGsonComponentSerializer implements GsonComponentSerializer {

  private final Gson gson;
  private final ComponentBuilder.Factory componentFactory;

  private final Map<HoverEvent.Action, HoverContentSerializer> hoverContentSerializers = new HashMap<>();

  public DefaultGsonComponentSerializer(Logger logger, ComponentBuilder.Factory componentFactory, boolean legacyHover) {
    this.componentFactory = componentFactory;

    this.gson = new GsonBuilder()
        .registerTypeHierarchyAdapter(ChatComponent.class, new GsonChatComponentSerializer(logger))
        .registerTypeAdapter(HoverEvent.class, legacyHover ?
            new LegacyHoverEventSerializer(logger, componentFactory, this) :
            new ModernHoverEventSerializer(logger, this, componentFactory))
        .create();

    this.registerHoverContentSerializer(HoverEvent.Action.SHOW_TEXT, new HoverTextSerializer());
    this.registerHoverContentSerializer(HoverEvent.Action.SHOW_ENTITY, new HoverEntitySerializer());
    this.registerHoverContentSerializer(HoverEvent.Action.SHOW_ACHIEVEMENT, this.getHoverContentSerializer(HoverEvent.Action.SHOW_TEXT));
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

  @Override
  public HoverContent deserializeHoverContent(ChatComponent component, HoverEvent.Action action) {
    return this.getHoverContentSerializer(action).deserialize(component, this.componentFactory, this.gson);
  }

  @Override
  public ChatComponent serializeHoverContent(HoverContent content) {
    return this.getHoverContentSerializer(content.getAction()).serialize(content, this.componentFactory, this.gson);
  }

  @Override
  public void registerHoverContentSerializer(HoverEvent.Action action, HoverContentSerializer serializer) {
    Preconditions.checkArgument(!this.hoverContentSerializers.containsKey(action), "A serializer for the action %s is already registered", action);

    this.hoverContentSerializers.put(action, serializer);
  }

  @Override
  public HoverContentSerializer getHoverContentSerializer(HoverEvent.Action action) {
    HoverContentSerializer serializer = this.hoverContentSerializers.get(action);
    if (serializer == null) {
      throw new UnsupportedOperationException("No serializer for the action " + action + " found");
    }
    return serializer;
  }
}
