package net.labyfy.internal.component.settings.serializer.defaults;

import com.google.gson.JsonObject;
import com.google.inject.Singleton;
import net.labyfy.component.settings.annotation.ui.Icon;
import net.labyfy.component.settings.registered.RegisteredSetting;
import net.labyfy.component.settings.serializer.SettingsSerializationHandler;
import net.labyfy.component.settings.serializer.SettingsSerializer;

@Singleton
@SettingsSerializer(Icon.class)
public class IconSerializer implements SettingsSerializationHandler<Icon> {
  @Override
  public void append(JsonObject result, RegisteredSetting setting, Icon annotation) {
    // TODO
  }
}
