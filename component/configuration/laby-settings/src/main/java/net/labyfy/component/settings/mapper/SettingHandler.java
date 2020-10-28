package net.labyfy.component.settings.mapper;

import net.labyfy.component.config.generator.method.ConfigObjectReference;

import java.lang.annotation.Annotation;

public interface SettingHandler<A extends Annotation> {

  // annotation has to have a defaultValue method
  Object getDefaultValue(A annotation, ConfigObjectReference reference);

  boolean isValidInput(Object input, ConfigObjectReference reference, A annotation);

}
