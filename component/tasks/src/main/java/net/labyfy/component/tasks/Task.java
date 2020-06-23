package net.labyfy.component.tasks;

import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.property.Property;
import net.labyfy.component.tasks.subproperty.TaskBody;

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

  boolean async() default true;
}
