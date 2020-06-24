package net.labyfy.component.tasks;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.*;

/**
 * Marks a method to be executed when a task is triggered.
 */
@Documented
@Identifier
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Transitive
public @interface Task {
  /**
   * @return task name to listen for
   */
  String value();

  /**
   * Should not be necessary to change in most cases.
   * But when changed, the trigger must be send to the chosen executor.
   *
   * @return task executor to use for handling task triggers
   */
  Class<? extends TaskExecutor> executor() default TaskExecutor.class;

  /**
   * Lowest priority will be called first
   *
   * @return task execution priority
   */
  double priority() default 0;
}
