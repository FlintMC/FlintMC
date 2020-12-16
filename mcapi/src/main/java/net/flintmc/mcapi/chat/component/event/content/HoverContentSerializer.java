package net.flintmc.mcapi.chat.component.event.content;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TextComponent;

/**
 * A serializer for {@link HoverContent}s. This is necessary because the contents are always
 * serialized differently. Minecraft uses the components to show them in the chat. For example
 * {@link HoverText} just contains the components, items are using a {@link TextComponent} with the
 * json data in its value.
 */
public interface HoverContentSerializer {

  /**
   * Deserializes the given component into a {@link HoverContent}, the gson instance should be able
   * to serialize/deserialize at least {@link TextComponent}s.
   *
   * @param serialized       The non-null component to be deserialized
   * @param componentFactory The non-null factory which might be used to create new components
   * @param gson             The non-null gson instance which might be used to deserialize {@link
   *                         ChatComponent}s
   * @return A new {@link HoverContent} or {@code null} if an invalid component has been provided
   * @throws JsonParseException If an error occurred while parsing the json out of the component
   */
  HoverContent deserialize(
      ChatComponent serialized, ComponentBuilder.Factory componentFactory, Gson gson)
      throws JsonParseException;

  /**
   * Serializes the given content into a {@link ChatComponent}, the gson instance should be able to
   * serialize/deserialize at least {@link TextComponent}s.
   *
   * @param content          The non-null content to be serialized
   * @param componentFactory The non-null factory which might be used to create new components
   * @param gson             The non-null gson instance which might be used to deserialize {@link
   *                         ChatComponent}s.
   * @return A new {@link ChatComponent} or {@code null} if an invalid content has been provided
   * @throws JsonParseException If an error occurred while parsing the json out of the component
   */
  ChatComponent serialize(
      HoverContent content, ComponentBuilder.Factory componentFactory, Gson gson)
      throws JsonParseException;
}
