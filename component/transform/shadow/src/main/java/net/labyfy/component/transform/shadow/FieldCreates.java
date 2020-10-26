package net.labyfy.component.transform.shadow;

import net.flintmc.processing.autoload.RepeatingDetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RepeatingDetectableAnnotation(FieldCreate.class)
public @interface FieldCreates {
  FieldCreate[] value();
}
