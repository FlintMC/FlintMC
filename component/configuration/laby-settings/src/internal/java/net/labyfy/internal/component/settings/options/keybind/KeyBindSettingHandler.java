package net.labyfy.internal.component.settings.options.keybind;

import com.google.gson.JsonObject;
import com.google.inject.Singleton;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.keybind.KeyBindSetting;
import net.labyfy.component.settings.options.keybind.PhysicalKey;
import net.labyfy.component.settings.registered.RegisteredSetting;

@Singleton
@RegisterSettingHandler(KeyBindSetting.class)
public class KeyBindSettingHandler implements SettingHandler<KeyBindSetting> {

  @Override
  public JsonObject serialize(KeyBindSetting annotation, RegisteredSetting setting) {
    JsonObject object = new JsonObject();
    PhysicalKey key = (PhysicalKey) setting.getCurrentValue();

    object.addProperty("value", key.name());
    // TODO add whether the KeyBind has duplicates

    return object;
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, KeyBindSetting annotation) {
    return input == null || input instanceof PhysicalKey;
  }
}
