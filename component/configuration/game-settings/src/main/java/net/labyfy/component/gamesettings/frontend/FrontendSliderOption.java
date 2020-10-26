package net.labyfy.component.gamesettings.frontend;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

/**
 * Represents an option slider for the settings frontend.
 */
public interface FrontendSliderOption extends FrontendOption {

  /**
   * Sets the range for the slider.
   *
   * @param min The minimum of the slider.
   * @param max The maximum of the slider.
   * @return This object, for chaining.
   */
  FrontendSliderOption setRange(int min, int max);

  /**
   * Retrieves the minimum value of an {@link Integer} slider.
   *
   * @return The minimum value of a slider.
   */
  int getMin();

  /**
   * Retrieves the maximum value of an {@link Integer} slider.
   *
   * @return The maximum value of a slider.
   */
  int getMax();

  /**
   * Sets the range for the slider.
   *
   * @param min The minimum value of the slider.
   * @param max The maximum value of the slider.
   * @return This object, for chaining.
   */
  FrontendSliderOption setRange(double min, double max);

  /**
   * Retrieves the minimum value of a {@link Double} slider.
   *
   * @return The minimum value of a slider.
   */
  double getMinValue();

  /**
   * Retrieves the maximum value of a {@link Double} slider.
   *
   * @return The maximum value of a slider.
   */
  double getMaxValue();

  /**
   * A factory class for the {@link FrontendSliderOption}.
   */
  @AssistedFactory(FrontendSliderOption.class)
  interface Factory {

    /**
     * Creates a new {@link FrontendSliderOption} with the given parameters.
     *
     * @param name         The configuration name of the option.
     * @param type         The option type.
     * @param defaultValue The default value for the option.
     * @return A created frontend slider option.
     */
    FrontendSliderOption create(
            @Assisted("name") String name,
            @Assisted("type") Class<?> type,
            @Assisted("defaultValue") String defaultValue
    );

  }

}
