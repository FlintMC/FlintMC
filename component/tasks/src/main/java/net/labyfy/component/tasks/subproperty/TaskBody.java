package net.labyfy.component.tasks.subproperty;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.*;

/**
 * Marks the execution method for a {@link net.labyfy.component.tasks.Task}.
 * Will be removed!
 */
@Deprecated
@Documented
@Identifier(requireParent = true)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Transitive
public @interface TaskBody {
  /**
   * Lowest priority will be called first
   *
   * @return task execution priority
   */
  double priority() default 0;
}
