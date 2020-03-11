package net.labyfy.base.task.property;

import net.labyfy.structure.identifier.Identifier;
import net.labyfy.structure.annotation.Transitive;
import net.labyfy.base.task.Task;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Identifier(parents = Task.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Transitive
public @interface TaskBodyPriority {

  double value() default 0;

}
