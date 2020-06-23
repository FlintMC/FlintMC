package net.labyfy.component.tasks.subproperty;

import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.annotation.Transitive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Identifier(requireParent = true)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Transitive
public @interface TaskBody {
  double priority() default 0;
}
