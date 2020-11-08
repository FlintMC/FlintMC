package net.flintmc.transform.shadow;

import net.flintmc.processing.autoload.DetectableAnnotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(FieldCreates.class)
@DetectableAnnotation(requiresParent = true)
public @interface FieldCreate {
  String name();

  String typeName();

  String defaultValue() default "";
}
