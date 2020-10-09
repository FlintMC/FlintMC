package net.labyfy.component.stereotype.identifier;

import net.labyfy.component.stereotype.annotation.Transitive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Transitive
public @interface Identifier {

  boolean requireParent() default false;

  Property[] requiredProperties() default {};

  Property[] optionalProperties() default {};

}
