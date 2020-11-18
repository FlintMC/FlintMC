package net.flintmc.mcapi.settings.flint.options.numeric;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines a range with a minimum, maximum value and a maximum number of decimal places for the
 * number.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Range {

  /**
   * Retrieves the minimum value of this range.
   *
   * @return The minimum value of this range
   */
  double min() default 0;

  /**
   * Retrieves the maximum value of this range.
   *
   * @return The maximum value of this range
   */
  double max();

  /**
   * Retrieves the number of decimal places that is allowed on the number in this range. If it is 0,
   * this means, that the number can only be an integer.
   *
   * @return The number of decimal places
   */
  int decimals() default 0;
}
