package net.labyfy.component.settings.mapper;

import com.google.gson.JsonObject;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.registered.RegisteredSetting;

import java.lang.annotation.Annotation;

public interface SettingHandler<A extends Annotation> {

  // annotation has to have a defaultValue method
  Object getDefaultValue(A annotation, ConfigObjectReference reference);

  // non-null
  JsonObject serialize(A annotation, RegisteredSetting setting);

  boolean isValidInput(Object input, ConfigObjectReference reference, A annotation);

}
