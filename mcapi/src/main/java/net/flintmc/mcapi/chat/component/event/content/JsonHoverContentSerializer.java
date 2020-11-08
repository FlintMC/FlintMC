package net.flintmc.mcapi.chat.component.event.content;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TextComponent;

/**
 * A serializer for {@link HoverContent}s which parses the text out of the given {@link
 * TextComponent} into a json on deserialization and the json into a {@link TextComponent} on
 * serialization.
 */
public abstract class JsonHoverContentSerializer implements HoverContentSerializer {

  protected abstract HoverContent deserializeJson(
      JsonElement element, ComponentBuilder.Factory componentFactory, Gson gson)
      throws JsonParseException;

  protected abstract JsonElement serializeJson(
      HoverContent content, ComponentBuilder.Factory componentFactory, Gson gson)
      throws JsonParseException;

  @Override
  public HoverContent deserialize(
      ChatComponent component, ComponentBuilder.Factory componentFactory, Gson gson)
      throws JsonParseException {
    if (!(component instanceof TextComponent)) {
      return null;
    }

    String text = ((TextComponent) component).text();
    JsonElement element = JsonParser.parseString(text);

    return this.deserializeJson(element, componentFactory, gson);
  }

  @Override
  public ChatComponent serialize(
      HoverContent content, ComponentBuilder.Factory componentFactory, Gson gson)
      throws JsonParseException {
    return componentFactory
        .text()
        .text(this.serializeJson(content, componentFactory, gson).toString())
        .build();
  }
}
