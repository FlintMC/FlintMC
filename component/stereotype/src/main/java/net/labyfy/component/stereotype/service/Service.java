package net.labyfy.component.stereotype.service;

import net.labyfy.component.processing.autoload.AutoLoad;

import java.lang.annotation.*;

import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.SERVICE_PRIORITY;
import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.SERVICE_ROUND;

/**
 * A service can be used to discover {@link IdentifierLegacy}s.
 * {@link Service} marks a class which implements {@link ServiceHandler}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AutoLoad(priority = SERVICE_PRIORITY, round = SERVICE_ROUND)
public @interface Service {

  /**
   * @return the target Identifier type
   */
  Class<?>[] value();

  /**
   * @return the loading priority. lower priorities are loaded first
   */
  int priority() default 0;
}
