package net.flintmc.mcapi.internal.settings.flint.serializer.defaults;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializationHandler;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializer;

@Singleton
@SettingsSerializer(DisplayName.class)
public class DisplayNameSerializer implements SettingsSerializationHandler<DisplayName> {

  private final ComponentSerializer.Factory serializerFactory;
  private final ComponentAnnotationSerializer annotationSerializer;

  @Inject
  public DisplayNameSerializer(
      ComponentSerializer.Factory serializerFactory,
      ComponentAnnotationSerializer annotationSerializer) {
    this.serializerFactory = serializerFactory;
    this.annotationSerializer = annotationSerializer;
  }

  @Override
  public void append(JsonObject result, RegisteredSetting setting, DisplayName annotation) {
    if (annotation == null || annotation.value().length == 0) {
      return;
    }

    ChatComponent component = this.annotationSerializer.deserialize(annotation.value());

    result.add("displayName", this.serializerFactory.gson().getGson().toJsonTree(component));
  }
}
