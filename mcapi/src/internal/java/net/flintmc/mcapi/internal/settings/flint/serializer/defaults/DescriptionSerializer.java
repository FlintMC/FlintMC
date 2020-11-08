package net.flintmc.mcapi.internal.settings.flint.serializer.defaults;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.settings.flint.annotation.ui.Description;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializer;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializationHandler;

@Singleton
@SettingsSerializer(Description.class)
public class DescriptionSerializer implements SettingsSerializationHandler<Description> {

  private final ComponentSerializer.Factory serializerFactory;
  private final ComponentAnnotationSerializer annotationSerializer;

  @Inject
  public DescriptionSerializer(ComponentSerializer.Factory serializerFactory, ComponentAnnotationSerializer annotationSerializer) {
    this.serializerFactory = serializerFactory;
    this.annotationSerializer = annotationSerializer;
  }

  @Override
  public void append(JsonObject result, RegisteredSetting setting, Description annotation) {
    if (annotation == null || annotation.value().length == 0) {
      return;
    }

    // the description is optional and may be displayed when hovering over the setting
    ChatComponent component = this.annotationSerializer.deserialize(annotation.value());

    result.add("displayName", this.serializerFactory.gson().getGson().toJsonTree(component));
  }
}
