package net.flintmc.mcapi.internal.settings.flint.serializer.defaults;

import com.google.gson.JsonObject;
import com.google.inject.Singleton;
import net.flintmc.mcapi.settings.flint.annotation.ui.Icon;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializationHandler;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializer;

@Singleton
@SettingsSerializer(Icon.class)
public class IconSerializer implements SettingsSerializationHandler<Icon> {
  @Override
  public void append(JsonObject result, RegisteredSetting setting, Icon annotation) {
    if (annotation == null) {
      return;
    }

    String value = annotation.value();
    if (value.isEmpty()) {
      return;
    }
    result.addProperty("icon", value);
  }
}
