package net.flintmc.mcapi.server.payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.processing.autoload.DetectableAnnotation;

/**
 * Marks a method as a payload channel receiver.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DetectableAnnotation
public @interface PayloadChannel {

  /**
   * Retrieves the namespace of the payload channel.
   *
   * @return The payload channel's namespace.
   */
  String namespace() default "flintmc";

  /**
   * Retrieves the path of the payload channel.
   *
   * @return The payload channel's path.
   */
  String path();
}
