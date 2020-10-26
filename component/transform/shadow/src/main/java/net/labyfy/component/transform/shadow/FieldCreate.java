package net.labyfy.component.transform.shadow;

import net.labyfy.component.processing.autoload.DetectableAnnotation;

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
