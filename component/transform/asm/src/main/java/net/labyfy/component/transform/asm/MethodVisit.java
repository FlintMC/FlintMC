package net.labyfy.component.transform.asm;

import net.labyfy.component.stereotype.annotation.Transitive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Transitive
public @interface MethodVisit {

  String className();

  String methodName();

  String desc() default "()V";
}
