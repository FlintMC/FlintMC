package net.labyfy.internal.component.settings.options.keybind;

import com.google.inject.Singleton;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.keybind.KeyBindSetting;
import net.labyfy.component.settings.options.keybind.PhysicalKey;

@Singleton
@RegisterSettingHandler(KeyBindSetting.class)
public class KeyBindSettingHandler implements SettingHandler<KeyBindSetting> {
  @Override
  public Object getDefaultValue(KeyBindSetting annotation, ConfigObjectReference reference) {
    return annotation.defaultValue();
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, KeyBindSetting annotation) {
    return input == null || input instanceof PhysicalKey;
  }
}
