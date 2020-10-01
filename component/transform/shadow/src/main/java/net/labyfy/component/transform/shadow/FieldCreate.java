package net.labyfy.component.transform.shadow;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Transitive
@Identifier(requireParent = true)
@Repeatable(FieldCreates.class)
public @interface FieldCreate {
  String name();

  String typeName();

  String defaultValue() default "";
}
