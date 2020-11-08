package net.flintmc.framework.config.defval.annotation;

import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.storage.ConfigStorage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation sets the default value of a method in a {@link Config}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DefaultString {

  /**
   * The default value which should be used if it is not set in a {@link ConfigStorage}.
   *
   * @return The default value
   */
  String value();

}
