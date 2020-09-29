package net.labyfy.internal.component.configuration;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.configuration.MethodSetting;
import net.labyfy.component.inject.implement.Implement;

import java.lang.reflect.Method;

@Implement(MethodSetting.class)
public class DefaultMethodSetting implements MethodSetting {

  private final Object instance;
  private final Method settingMethod;
  private final String settingName;

  @AssistedInject
  private DefaultMethodSetting(
          @Assisted("instance") Object instance,
          @Assisted("settingMethod") Method settingMethod,
          @Assisted("settingName") String settingName
  ) {
    this.instance = instance;
    this.settingMethod = settingMethod;
    this.settingName = settingName;
  }

  @Override
  public Object getInstance() {
    return this.instance;
  }

  @Override
  public Method getSettingMethod() {
    return this.settingMethod;
  }

  @Override
  public String getSettingName() {
    return this.settingName;
  }

}
