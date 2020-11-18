package net.flintmc.mcapi.settings.flint.options.numeric.display;

import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.options.numeric.NumericSetting;
import net.flintmc.mcapi.settings.flint.options.numeric.SliderSetting;

import java.lang.annotation.*;

/**
 * Represents a {@link Component} mapped to an integer
 *
 * @see NumericSetting
 * @see SliderSetting
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(NumericDisplays.class)
public @interface NumericDisplay {

  /**
   * Retrieves the component that should be displayed instead of the number
   *
   * @return The component to be displayed
   */
  Component display();

  /**
   * Retrieves the number that should be replaced with the {@link #display()}.
   *
   * @return The number to be replaced
   */
  int value();
}
