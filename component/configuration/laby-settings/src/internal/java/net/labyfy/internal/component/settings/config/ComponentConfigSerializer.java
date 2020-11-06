package net.labyfy.internal.component.settings.config;

import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.serializer.ComponentSerializer;
import net.labyfy.chat.serializer.GsonComponentSerializer;
import net.labyfy.component.config.serialization.ConfigSerializationHandler;
import net.labyfy.component.config.serialization.ConfigSerializer;

@Singleton
@ConfigSerializer(ChatComponent.class)
public class ComponentConfigSerializer implements ConfigSerializationHandler<ChatComponent> {

  private final GsonComponentSerializer serializer;

  @Inject
  public ComponentConfigSerializer(ComponentSerializer.Factory serializerFactory) {
    this.serializer = serializerFactory.gson();
  }

  @Override
  public JsonElement serialize(ChatComponent chatComponent) {
    return this.serializer.getGson().toJsonTree(chatComponent);
  }

  @Override
  public ChatComponent deserialize(JsonElement element) {
    return this.serializer.getGson().fromJson(element, ChatComponent.class);
  }
}
