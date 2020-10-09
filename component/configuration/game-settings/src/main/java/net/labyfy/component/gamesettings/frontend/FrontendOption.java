package net.labyfy.component.gamesettings.frontend;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

/**
 * Represents an option for the settings frontend.
 */
public interface FrontendOption {

  /**
   * Retrieves the configuration name of the option.
   *
   * @return The option configuration name.
   */
  String getConfigurationName();

  /**
   * Retrieves the type of the option.
   *
   * @return The option type.
   */
  Class<?> getType();

  /**
   * Retrieves the default value of the option.
   *
   * @return The default value.
   */
  String getDefaultValue();

  /*

  SLIDER

   */

  /**
   * Sets the range for the slider.
   *
   * @param min The minimum of the slider.
   * @param max The maximum of the slider.
   * @return This object, for chaining.
   */
  FrontendOption setRange(int min, int max);

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
  FrontendOption setRange(double min, double max);

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
   * A factory class for {@link FrontendOption}.
   */
  @AssistedFactory(FrontendOption.class)
  interface Factory {

    /**
     * Creates a new {@link FrontendOption} with the given parameters.
     *
     * @param name         The configuration name of the option.
     * @param type         The option type.
     * @param defaultValue The default value for the option.
     * @return A created frontend option.
     */
    FrontendOption create(
            @Assisted("name") String name,
            @Assisted("type") Class<?> type,
            @Assisted("defaultValue") String defaultValue
    );

  }

}
