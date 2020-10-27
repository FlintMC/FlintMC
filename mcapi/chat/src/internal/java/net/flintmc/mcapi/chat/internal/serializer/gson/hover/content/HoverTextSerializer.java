package net.flintmc.mcapi.chat.internal.serializer.gson.hover.content;

import com.google.gson.Gson;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.chat.component.event.content.HoverText;
import net.flintmc.mcapi.chat.component.event.content.HoverContentSerializer;

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
