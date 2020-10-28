package net.labyfy.internal.component.settings.options;

import com.google.inject.Singleton;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.BooleanSetting;

@Singleton
@RegisterSettingHandler(BooleanSetting.class)
public class BooleanSettingHandler implements SettingHandler<BooleanSetting> {

  @Override
  public Object getDefaultValue(BooleanSetting annotation, ConfigObjectReference reference) {
    return annotation.defaultValue();
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, BooleanSetting annotation) {
    return input instanceof Boolean;
  }
}
