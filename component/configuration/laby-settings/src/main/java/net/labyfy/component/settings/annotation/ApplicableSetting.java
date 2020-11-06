package net.labyfy.component.settings.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// annotation for all specific settings like BooleanSetting, SliderSetting, ...
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ApplicableSetting {

  String type();

  // ConfigObjectReference#getSerializedType, needs to be assignable to at least one of the required return types
  Class<?>[] value();

}
