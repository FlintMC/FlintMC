package net.labyfy.component.gamesettings.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Fired when the configuration is loaded or saved.
 */
public interface ConfigurationEvent {

  /**
   * Retrieves the state of this event.
   *
   * @return The current state.
   */
  @Named("state")
  State getState();

  /**
   * Retrieves the `options.txt` as a file.
   *
   * @return The `options.txt` as a file.
   */
  File getOptionsFile();

  /**
   * Changes the options file.
   *
   * @param optionsFile The new options file.
   */
  void setOptionsFile(File optionsFile);

  /**
   * Retrieves the options in the file as a key-value system.
   *
   * @return The options as a key-value system.
   */
  Map<String, String> getConfigurations();

  /**
   * Changes the configurations key-value system.
   *
   * @param configurations The new key-value system.
   */
  void setConfigurations(Map<String, String> configurations);

  /**
   * An enumeration that representing all states for the configuration.
   */
  enum State {

    /**
     * When the configuration is loaded.
     */
    LOAD,
    /**
     * When the configuration is saved.
     */
    SAVE

  }

  /**
   * The {@link EventGroup} annotation to filter {@link ConfigurationEvent}'s by their option state.
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @interface OptionState {

    /**
     * Retrieves the state of the option file.
     *
     * @return The non-null state.
     */
    @Named("state")
    State value();

  }

  /**
   * A factory class for the {@link ConfigurationEvent}.
   */
  @AssistedFactory(ConfigurationEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ConfigurationEvent} with the given parameters.
     *
     * @param state          The state when the event is fired.
     * @param optionsFile    The options file
     * @param configurations The configuration.
     * @return A created configuration event.
     */
    ConfigurationEvent create(
            @Assisted("state") State state,
            @Assisted("optionsFile") File optionsFile,
            @Assisted("configurations") Map<String, String> configurations
    );

  }

}
