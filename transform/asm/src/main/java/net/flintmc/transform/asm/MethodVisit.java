package net.flintmc.transform.asm;

import net.flintmc.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DetectableAnnotation
public @interface MethodVisit {

  String className();

  String methodName();

  String desc() default "()V";
}
