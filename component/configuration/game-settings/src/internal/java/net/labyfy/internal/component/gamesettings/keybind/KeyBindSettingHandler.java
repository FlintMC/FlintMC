package net.labyfy.internal.component.gamesettings.keybind;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.gamesettings.configuration.KeyBindingConfiguration;
import net.labyfy.component.gamesettings.keybind.KeyBindSetting;
import net.labyfy.component.gamesettings.keybind.PhysicalKey;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.registered.RegisteredSetting;

@Singleton
@RegisterSettingHandler(KeyBindSetting.class)
public class KeyBindSettingHandler implements SettingHandler<KeyBindSetting> {

  private final KeyBindingConfiguration configuration;

  @Inject
  public KeyBindSettingHandler(KeyBindingConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public JsonObject serialize(KeyBindSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();
    PhysicalKey key = (PhysicalKey) currentValue;
    if (key == null) {
      key = PhysicalKey.UNKNOWN;
    }

    object.addProperty("value", key.name());
    object.addProperty("duplicates", this.configuration.hasDuplicates(key));

    return object;
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, KeyBindSetting annotation) {
    return input == null || input instanceof PhysicalKey;
  }
}
