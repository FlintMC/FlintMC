package net.labyfy.component.transform.shadow;

import net.labyfy.component.stereotype.annotation.Transitive;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Transitive
@Repeatable(FieldCreates.class)
public @interface FieldCreate {
  String name();

  String typeName();

  String defaultValue() default "";
}
