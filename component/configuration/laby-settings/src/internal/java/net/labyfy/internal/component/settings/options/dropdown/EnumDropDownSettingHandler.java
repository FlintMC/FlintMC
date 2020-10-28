package net.labyfy.internal.component.settings.options.dropdown;

import com.google.inject.Singleton;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.InvalidSettingsException;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.dropdown.EnumDropDownSetting;

import java.lang.reflect.Type;

@Singleton
@RegisterSettingHandler(EnumDropDownSetting.class)
public class EnumDropDownSettingHandler implements SettingHandler<EnumDropDownSetting> {
  @Override
  public Object getDefaultValue(EnumDropDownSetting annotation, ConfigObjectReference reference) {
    Type type = reference.getSerializedType();
    if (!(type instanceof Class) || !Enum.class.isAssignableFrom((Class<?>) type)) {
      throw new InvalidSettingsException("Cannot use EnumDropDownSetting without Enum as the return type, " +
          "return type was: " + type.getTypeName());
      // should never occur because this is already checked in the SettingsDiscoverer
    }
    return ((Class<?>) type).getEnumConstants()[annotation.defaultValue()];
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, EnumDropDownSetting annotation) {
    return input == null || reference.getSerializedType().equals(input.getClass());
  }
}
