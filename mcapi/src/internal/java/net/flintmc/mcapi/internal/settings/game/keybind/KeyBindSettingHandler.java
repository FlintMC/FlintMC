package net.flintmc.mcapi.internal.gamesettings.keybind;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.game.configuration.KeyBindingConfiguration;
import net.flintmc.mcapi.settings.game.keybind.KeyBindSetting;
import net.flintmc.mcapi.settings.game.keybind.PhysicalKey;

@Singleton
@RegisterSettingHandler(KeyBindSetting.class)
public class KeyBindSettingHandler implements SettingHandler<KeyBindSetting> {

  private final KeyBindingConfiguration configuration;

  @Inject
  public KeyBindSettingHandler(KeyBindingConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public JsonObject serialize(
      KeyBindSetting annotation, RegisteredSetting setting, Object currentValue) {
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
  public boolean isValidInput(
      Object input, ConfigObjectReference reference, KeyBindSetting annotation) {
    return input == null || input instanceof PhysicalKey;
  }
}
