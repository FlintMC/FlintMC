package net.labyfy.component.stereotype.service;

import net.flintmc.processing.autoload.DetectableAnnotation;

import java.lang.annotation.*;

/**
 * A service can be used to discover {@link DetectableAnnotation}s.
 * {@link Service} marks a class which implements {@link ServiceHandler}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface Service {

  /**
   * @return the target Identifier type
   */
  Class<? extends Annotation>[] value();

  /**
   * @return the loading priority. lower priorities are loaded first
   */
  int priority() default 0;

  State state() default State.POST_INIT;

  enum State {
    PRE_INIT,
    POST_INIT
  }

}
