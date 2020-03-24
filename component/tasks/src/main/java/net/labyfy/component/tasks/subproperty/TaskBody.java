package net.labyfy.component.tasks.subproperty;

import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.annotation.Transitive;
import net.labyfy.base.structure.property.Property;
import net.labyfy.component.tasks.Task;

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
