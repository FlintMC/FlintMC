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
public @interface DefaultEnum {

  /**
   * The default value which should be used if it is not set in a {@link ConfigStorage}. Since this sets an enum, this
   * retrieves the index of the enum constant that should be used as the default value.
   *
   * @return The index of the enum constant to be used as a default value
   */
  int value();

}
