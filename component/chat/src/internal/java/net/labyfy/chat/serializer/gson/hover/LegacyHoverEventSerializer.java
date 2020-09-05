package net.labyfy.chat.serializer.gson.hover;

import com.google.common.base.Preconditions;
import com.google.gson.*;
import net.labyfy.chat.builder.DefaultTextComponentBuilder;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.component.TextComponent;
import net.labyfy.chat.component.event.HoverEvent;
import net.labyfy.chat.component.event.content.HoverContent;
import net.labyfy.chat.component.event.content.HoverEntity;
import net.labyfy.chat.component.event.content.HoverItem;
import net.labyfy.chat.component.event.content.HoverText;
import net.labyfy.chat.serializer.ComponentSerializer;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * The serializer for HoverEvents in minecraft versions below 1.16.
 */
public class LegacyHoverEventSerializer extends HoverEventSerializer {

  private final ComponentSerializer.Factory factory;

  public LegacyHoverEventSerializer(ComponentSerializer.Factory factory, Logger logger) {
    super(logger);
    this.factory = factory;
  }

  @Override
  public HoverEvent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    if (!json.isJsonObject()) {
      return null;
    }

    JsonObject object = json.getAsJsonObject();
    HoverEvent.Action action = super.deserializeAction(object);
    if (action == null) {
      return null;
    }

    JsonElement value = object.get("value");
    ChatComponent component = value.isJsonPrimitive() ? (TextComponent) new DefaultTextComponentBuilder().text(value.getAsString()).build() : context.deserialize(value, ChatComponent.class);
    if (component == null) {
      return null;
    }

    switch (action) {
      case SHOW_TEXT:
      case SHOW_ACHIEVEMENT:
        return HoverEvent.text(component);

      case SHOW_ITEM:
        JsonObject item = JsonParser.parseString(((TextComponent) component).text()).getAsJsonObject();
        JsonElement tag = item.get("tag");

        return HoverEvent.item(item.get("id").getAsString(), item.get("Count").getAsInt(), tag != null ? tag.toString() : null);

      case SHOW_ENTITY:
        JsonObject entity = JsonParser.parseString(((TextComponent) component).text()).getAsJsonObject();

        UUID uniqueId = context.deserialize(entity.get("id"), UUID.class);
        ChatComponent display = entity.has("name") ?
            new DefaultTextComponentBuilder().text(entity.get("name").getAsString()).build() :
            null;

        return uniqueId != null ? HoverEvent.entity(uniqueId, entity.get("type").getAsString(), display) : null;

      default:
        return null;
    }
  }

  @Override
  public JsonElement serialize(HoverEvent src, Type typeOfSrc, JsonSerializationContext context) {
    Preconditions.checkArgument(src.getContents().length == 1, "Legacy hover events cannot have multiple contents");

    JsonObject object = new JsonObject();

    object.addProperty("action", src.getContents()[0].getAction().getLowerName());

    ChatComponent component;

    HoverContent content = src.getContents()[0];
    switch (content.getAction()) {
      case SHOW_TEXT:
      case SHOW_ACHIEVEMENT:
        component = ((HoverText) content).getText();
        break;

      case SHOW_ENTITY:
        HoverEntity entity = (HoverEntity) content;
        JsonObject entityObject = new JsonObject();

        entityObject.addProperty("id", entity.getUniqueId().toString());
        entityObject.addProperty("type", entity.getType());
        if (entity.getDisplayName() != null) {
          // the legacy entity uses the legacy text as a display name instead of the json
          entityObject.addProperty("name", this.factory.legacy().serialize(entity.getDisplayName()));
        }

        component = new DefaultTextComponentBuilder().text(entityObject.toString()).build();
        break;

      case SHOW_ITEM:
        HoverItem item = (HoverItem) content;
        JsonObject itemObject = new JsonObject();

        itemObject.addProperty("id", item.getId());
        itemObject.addProperty("Count", item.getCount());
        if (item.getNbt() != null) {
          itemObject.add("tag", JsonParser.parseString(item.getNbt()));
        }

        component = new DefaultTextComponentBuilder().text(itemObject.toString()).build();
        break;

      default:
        return null;
    }

    object.add("value", context.serialize(component));

    return object;
  }
}
