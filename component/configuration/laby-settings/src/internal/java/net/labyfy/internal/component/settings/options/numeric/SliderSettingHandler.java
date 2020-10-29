package net.labyfy.internal.component.settings.options.numeric;

import com.google.inject.Singleton;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.numeric.NumericRestriction;
import net.labyfy.component.settings.options.numeric.Range;
import net.labyfy.component.settings.options.numeric.SliderSetting;

@Singleton
@RegisterSettingHandler(SliderSetting.class)
public class SliderSettingHandler extends RangedSettingHandler implements SettingHandler<SliderSetting> {
  @Override
  public Object getDefaultValue(SliderSetting annotation, ConfigObjectReference reference) {
    double value = annotation.defaultValue();
    Range range = annotation.value();
    if (super.inRange(reference, range, value)) {
      return value;
    }

    double newValue = (range.max() - range.min()) / 2;
    if (!super.hasRestriction(range, NumericRestriction.ALLOW_DECIMALS)) {
      newValue = Math.floor(newValue);
    }

    return newValue;
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, SliderSetting annotation) {
    return super.inRange(reference, annotation.value(), input);
  }
}
