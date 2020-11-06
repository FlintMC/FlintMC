package net.labyfy.internal.component.settings.options.text;

import com.google.gson.JsonObject;
import com.google.inject.Singleton;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.text.CharSetting;
import net.labyfy.component.settings.registered.RegisteredSetting;

@Singleton
@RegisterSettingHandler(CharSetting.class)
public class CharSettingHandler implements SettingHandler<CharSetting> {

  @Override
  public JsonObject serialize(CharSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();

    object.addProperty("value", currentValue == null ? ' ' : (char) currentValue);

    return object;
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, CharSetting annotation) {
    return input instanceof Character;
  }
}
