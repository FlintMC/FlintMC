package net.flintmc.framework.inject.implement;

import net.flintmc.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as an implementation of an interface. This class will then be made available for injection
 * where injection parameters with the interface type are found.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DetectableAnnotation
public @interface Implement {
  /**
   * The interface implemented by this class. This means, the class also needs to {@code implements} the given
   * interface.
   *
   * @return The interface implemented by this class
   */
  Class<?> value();

  /**
   * The minecraft version this {@code @Implement} applies to. If the version does not match, the implementation
   * is not bound.
   *
   * @return The version this {@code @Implement} applies to
   */
  String version() default "";
}
