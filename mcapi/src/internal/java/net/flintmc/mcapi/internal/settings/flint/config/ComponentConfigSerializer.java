package net.flintmc.mcapi.internal.settings.flint.config;

import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.serialization.ConfigSerializationHandler;
import net.flintmc.framework.config.serialization.ConfigSerializer;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.chat.serializer.GsonComponentSerializer;

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
