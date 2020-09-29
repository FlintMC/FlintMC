package net.labyfy.component.transform.shadow;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Identifier(requireParent = true)
@Transitive
public @interface FieldCreate {
  String name();

  String typeName();

  String defaultValue() default "";
}
