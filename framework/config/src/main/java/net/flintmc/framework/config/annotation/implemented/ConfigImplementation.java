package net.flintmc.framework.config.annotation.implemented;

import net.flintmc.framework.config.annotation.Config;
import net.flintmc.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The counterpart to {@link ImplementedConfig}. Use it on the implementations of interfaces annotated with {@link
 * ImplementedConfig} to mark them as their implementation.
 *
 * @see Config
 * @see ImplementedConfig
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface ConfigImplementation {

  /**
   * The interface implemented by this class. This means, the class also needs to {@code implements} the given interface
   * and have the {@link ImplementedConfig} annotation.
   *
   * @return The interface implemented by this class
   */
  Class<?> value();

  /**
   * The minecraft version this {@code @Implement} applies to. If the version does not match, the implementation is not
   * bound.
   *
   * @return The version this {@code @Implement} applies to
   */
  String version() default "";

}
