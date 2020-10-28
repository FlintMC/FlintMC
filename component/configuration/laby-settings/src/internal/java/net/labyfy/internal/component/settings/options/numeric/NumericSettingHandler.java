package net.labyfy.internal.component.settings.options.numeric;

import com.google.inject.Singleton;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.numeric.NumericSetting;

@Singleton
@RegisterSettingHandler(NumericSetting.class)
public class NumericSettingHandler extends RangedSettingHandler implements SettingHandler<NumericSetting> {
  @Override
  public Object getDefaultValue(NumericSetting annotation, ConfigObjectReference reference) {
    return annotation.defaultValue();
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, NumericSetting annotation) {
    return super.inRange(reference, annotation.value(), input);
  }
}
