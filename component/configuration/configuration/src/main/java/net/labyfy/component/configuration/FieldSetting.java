package net.labyfy.component.configuration;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.lang.reflect.Field;

public interface FieldSetting {

  Object getInstance();

  Field getSettingField();

  String getSettingName();

  @AssistedFactory(FieldSetting.class)
  interface Factory {

    FieldSetting create(
            @Assisted("instance") Object instance,
            @Assisted("settingField") Field settingField,
            @Assisted("settingName") String settingName
    );

  }

}
