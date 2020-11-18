package net.flintmc.mcapi.settings.flint.options.numeric;

import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.options.numeric.display.NumericDisplay;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The same as {@link NumericDisplay}, but displayed as a slider instead of an input field.
 *
 * @see ApplicableSetting
 * @see NumericDisplay
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(
    types = {byte.class, short.class, int.class, long.class, double.class, float.class},
    name = "slider")
public @interface SliderSetting {

  /**
   * Retrieves the range in which numbers may be specified.
   *
   * @return The range for the value
   */
  Range value();
}
