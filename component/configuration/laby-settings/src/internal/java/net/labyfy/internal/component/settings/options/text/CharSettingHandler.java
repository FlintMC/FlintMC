package net.labyfy.internal.component.settings.options.text;

import com.google.inject.Singleton;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.text.CharSetting;

@Singleton
@RegisterSettingHandler(CharSetting.class)
public class CharSettingHandler implements SettingHandler<CharSetting> {
  @Override
  public Object getDefaultValue(CharSetting annotation, ConfigObjectReference reference) {
    return annotation.defaultValue();
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, CharSetting annotation) {
    return input instanceof Character;
  }
}
