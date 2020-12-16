package net.flintmc.mcapi.chat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.storage.ConfigStorage;

/**
 * This annotation sets the default value of a method in a {@link Config}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DefaultComponent {

  /**
   * The default value which should be used if it is not set in a {@link ConfigStorage}.
   *
   * @return The default value
   */
  Component[] value();
}
