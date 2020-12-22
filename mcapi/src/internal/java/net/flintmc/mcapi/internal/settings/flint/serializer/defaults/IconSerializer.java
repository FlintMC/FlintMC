package net.flintmc.mcapi.internal.settings.flint.serializer.defaults;

import com.google.gson.JsonObject;
import com.google.inject.Singleton;
import net.flintmc.mcapi.settings.flint.annotation.ui.icon.Icon;
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

    JsonObject icon = new JsonObject();
    result.add("icon", icon);

    if (!annotation.item().value().isEmpty()) {
      icon.addProperty("type", annotation.item().value());
      icon.addProperty("amount", annotation.item().amount());
      icon.addProperty("enchanted", annotation.item().enchanted());
      return;
    }

    if (!annotation.resource().isEmpty()) {
      String url = "file:///$resource/" + annotation.resource();
      icon.addProperty("url", url);
      return;
    }

    if (!annotation.url().isEmpty()) {
      icon.addProperty("url", annotation.url());
      return;
    }

    if (!annotation.html().isEmpty()) {
      icon.addProperty("html", annotation.html());
    }
  }
}
