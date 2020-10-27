package net.flintmc.mcapi.items.internal.component;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.chat.component.event.content.JsonHoverContentSerializer;
import net.flintmc.mcapi.items.ItemStackSerializer;
import net.flintmc.mcapi.items.component.HoverItem;

/**
 * Serializer for {@link HoverItem}
 */
public class HoverItemSerializer extends JsonHoverContentSerializer {

  private final ItemStackSerializer itemStackSerializer;

  public HoverItemSerializer(ItemStackSerializer itemStackSerializer) {
    this.itemStackSerializer = itemStackSerializer;
  }

  @Override
  protected HoverContent deserializeJson(JsonElement element, ComponentBuilder.Factory componentFactory, Gson gson) throws JsonParseException {
    if (!element.isJsonObject()) {
      return null;
    }
    return new HoverItem(this.itemStackSerializer.fromJson(element.getAsJsonObject()));
  }

  @Override
  protected JsonElement serializeJson(HoverContent content, ComponentBuilder.Factory componentFactory, Gson gson) throws JsonParseException {
    HoverItem item = (HoverItem) content;
    return this.itemStackSerializer.toJson(item.getItemStack());
  }

}
