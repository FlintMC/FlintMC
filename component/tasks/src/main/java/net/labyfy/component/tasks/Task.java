package net.labyfy.component.tasks;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.property.Property;
import net.labyfy.component.tasks.subproperty.TaskBody;

import java.lang.annotation.*;

/**
 * Marks the existence of one or more {@link TaskBody}.
 * Will be redesigned and redocumented later.
 */
@Deprecated
@Documented
@Identifier(requiredProperties = @Property(value = TaskBody.class, allowMultiple = true))
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Transitive
public @interface Task {
  String value();

  Class<? extends TaskExecutor> executor() default TaskExecutor.class;

  boolean async() default true;
}
