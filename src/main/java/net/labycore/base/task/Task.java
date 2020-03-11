package net.labycore.base.task;

import net.labycore.structure.identifier.Identifier;
import net.labycore.structure.annotation.Transitive;
import net.labycore.structure.property.Property;
import net.labycore.base.task.property.TaskBody;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Identifier(requiredProperties = @Property(value = TaskBody.class, allowMultiple = true))
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Transitive
public @interface Task {
  String value();

  Class<? extends TaskExecutor> executor() default TaskExecutor.class;
}
