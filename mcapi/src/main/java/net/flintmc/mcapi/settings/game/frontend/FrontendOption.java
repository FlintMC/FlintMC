package net.flintmc.mcapi.settings.game.frontend;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/** Represents an option for the settings frontend. */
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

  /** A factory class for {@link FrontendOption}. */
  @AssistedFactory(FrontendOption.class)
  interface Factory {

    /**
     * Creates a new {@link FrontendOption} with the given parameters.
     *
     * @param name The configuration name of the option.
     * @param type The option type.
     * @param defaultValue The default value for the option.
     * @return A created frontend option.
     */
    FrontendOption create(
        @Assisted("name") String name,
        @Assisted("type") Class<?> type,
        @Assisted("defaultValue") String defaultValue);
  }
}
