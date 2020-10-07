package net.labyfy.component.stereotype.identifier;

import net.labyfy.component.stereotype.annotation.Transitive;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Transitive
public @interface Property {

  Class<? extends Annotation> value();

  boolean allowMultiple() default false;

}
