package net.labyfy.component.settings.mapper;

import com.google.gson.JsonObject;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.registered.RegisteredSetting;

import java.lang.annotation.Annotation;

public interface SettingHandler<A extends Annotation> {

  // non-null
  JsonObject serialize(A annotation, RegisteredSetting setting, Object currentValue);

  boolean isValidInput(Object input, ConfigObjectReference reference, A annotation);

}
