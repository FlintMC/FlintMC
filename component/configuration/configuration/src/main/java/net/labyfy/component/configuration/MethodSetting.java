package net.labyfy.component.configuration;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.lang.reflect.Method;

public interface MethodSetting {

  Object getInstance();

  Method getSettingMethod();

  String getSettingName();

  @AssistedFactory(MethodSetting.class)
  interface Factory {

    MethodSetting create(
            @Assisted("instance") Object instance,
            @Assisted("settingMethod") Method settingMethod,
            @Assisted("settingName") String settingName
    );

  }

}
