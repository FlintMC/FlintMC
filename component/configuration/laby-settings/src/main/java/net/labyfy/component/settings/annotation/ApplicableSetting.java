package net.labyfy.component.settings.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// annotation for all specific settings like BooleanSetting, SliderSetting, ...
// annotated annotations need to have a defaultValue() method which is either assignable to the value in this annotation
// or has a mapper which returns a value for the defaultValue which is assignable to the value in this annotation
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ApplicableSetting {

  // ConfigObjectReference#getSerializedType, needs to be assignable to at least one of the required return types
  Class<?>[] value();

}
