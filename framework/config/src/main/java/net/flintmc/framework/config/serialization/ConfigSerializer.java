package net.flintmc.framework.config.serialization;

import net.flintmc.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a {@link ConfigSerializationHandler} to be registered in the {@link ConfigSerializationService}.
 *
 * @see ConfigSerializationHandler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface ConfigSerializer {

  /**
   * The class which can be serialized by this handler.
   *
   * @return The class which can be serialized by this handler
   */
  Class<?> value();

}
