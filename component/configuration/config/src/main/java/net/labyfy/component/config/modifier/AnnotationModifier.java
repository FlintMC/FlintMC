package net.labyfy.component.config.modifier;

import net.labyfy.component.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@DetectableAnnotation
public @interface AnnotationModifier {

  Class<?> value();

  String method();

}
