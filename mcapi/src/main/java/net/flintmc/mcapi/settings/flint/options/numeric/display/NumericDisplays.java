package net.flintmc.mcapi.settings.flint.options.numeric.display;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Repeatable {@link NumericDisplay}.
 *
 * @see NumericDisplay
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NumericDisplays {

  /**
   * Retrieves all numeric displays that are wrapped by this annotation.
   *
   * @return The numeric displays
   */
  NumericDisplay[] value();
}
