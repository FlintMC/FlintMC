package net.labyfy.internal.component.settings.serializer.defaults;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.annotation.ComponentAnnotationSerializer;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.serializer.ComponentSerializer;
import net.labyfy.component.settings.annotation.ui.Description;
import net.labyfy.component.settings.registered.RegisteredSetting;
import net.labyfy.component.settings.serializer.SettingsSerializationHandler;
import net.labyfy.component.settings.serializer.SettingsSerializer;

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
