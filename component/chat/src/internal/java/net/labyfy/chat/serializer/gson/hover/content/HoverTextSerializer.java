package net.labyfy.chat.serializer.gson.hover.content;

import com.google.gson.Gson;
import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.component.event.content.HoverContent;
import net.labyfy.chat.component.event.content.HoverContentSerializer;
import net.labyfy.chat.component.event.content.HoverText;

/**
 * Serializer for {@link HoverText}
 */
public class HoverTextSerializer implements HoverContentSerializer {

  @Override
  public HoverContent deserialize(ChatComponent component, ComponentBuilder.Factory componentFactory, Gson gson) {
    return new HoverText(component);
  }

  @Override
  public ChatComponent serialize(HoverContent content, ComponentBuilder.Factory componentFactory, Gson gson) {
    HoverText text = (HoverText) content;

    return text.getText();
  }
}
