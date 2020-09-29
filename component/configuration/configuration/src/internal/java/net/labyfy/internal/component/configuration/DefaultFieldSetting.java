package net.labyfy.internal.component.configuration;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.configuration.FieldSetting;
import net.labyfy.component.inject.implement.Implement;

import java.lang.reflect.Field;

@Implement(FieldSetting.class)
public class DefaultFieldSetting implements FieldSetting {

  private final Object instance;
  private final Field settingField;
  private final String settingName;

  @AssistedInject
  public DefaultFieldSetting(
          @Assisted("instance") Object instance,
          @Assisted("settingField") Field settingField,
          @Assisted("settingName") String settingName
  ) {
    this.instance = instance;
    this.settingField = settingField;
    this.settingName = settingName;
  }

  @Override
  public Object getInstance() {
    return this.instance;
  }

  @Override
  public Field getSettingField() {
    return this.settingField;
  }

  @Override
  public String getSettingName() {
    return this.settingName;
  }
}
