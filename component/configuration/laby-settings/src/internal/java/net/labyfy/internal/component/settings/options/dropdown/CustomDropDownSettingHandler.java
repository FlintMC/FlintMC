package net.labyfy.internal.component.settings.options.dropdown;

import com.google.inject.Singleton;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.dropdown.CustomDropDownSetting;

import java.util.Arrays;

@Singleton
@RegisterSettingHandler(CustomDropDownSetting.class)
public class CustomDropDownSettingHandler implements SettingHandler<CustomDropDownSetting> {
  @Override
  public Object getDefaultValue(CustomDropDownSetting annotation, ConfigObjectReference reference) {
    return annotation.defaultValue();
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, CustomDropDownSetting annotation) {
    return input == null || (input instanceof String && Arrays.asList(annotation.value()).contains(input));
  }
}
