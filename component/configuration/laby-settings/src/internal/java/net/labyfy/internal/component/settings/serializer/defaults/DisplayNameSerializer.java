package net.labyfy.internal.component.settings.serializer.defaults;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.annotation.ComponentAnnotationSerializer;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.serializer.ComponentSerializer;
import net.labyfy.component.settings.annotation.ui.DisplayName;
import net.labyfy.component.settings.registered.RegisteredSetting;
import net.labyfy.component.settings.serializer.SettingsSerializationHandler;
import net.labyfy.component.settings.serializer.SettingsSerializer;

@Singleton
@SettingsSerializer(DisplayName.class)
public class DisplayNameSerializer implements SettingsSerializationHandler<DisplayName> {

  private final ComponentSerializer.Factory serializerFactory;
  private final ComponentAnnotationSerializer annotationSerializer;

  @Inject
  public DisplayNameSerializer(ComponentSerializer.Factory serializerFactory, ComponentAnnotationSerializer annotationSerializer) {
    this.serializerFactory = serializerFactory;
    this.annotationSerializer = annotationSerializer;
  }

  @Override
  public void append(JsonObject result, RegisteredSetting setting, DisplayName annotation) {
    result.add("displayName", this.serialize(annotation,
        setting.getReference().getPathKeys()[setting.getReference().getPathKeys().length - 1]));


  }

  private JsonElement serialize(DisplayName annotation, String optional) {
    ChatComponent component;

    // a displayName is necessary, if no displayName has been provided, we use the last value from the path keys
    // which is part of the name of the getter/setter
    if (annotation == null) {
      component = this.serializerFactory.legacy().deserialize(optional);
    } else {
      component = this.annotationSerializer.deserialize(annotation.value());
    }

    return this.serializerFactory.gson().getGson().toJsonTree(component);
  }

}
