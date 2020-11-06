package net.labyfy.component.config.event;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.storage.ConfigStorage;
import net.labyfy.component.config.storage.ConfigStorageProvider;
import net.labyfy.component.eventbus.event.Event;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.assisted.AssistedFactory;

/**
 * This event will be fired in both the {@link Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phases whenever a
 * {@link ParsedConfig} is being written in/read from all {@link ConfigStorage}s in a {@link ConfigStorageProvider}.
 *
 * @see Subscribe
 */
public interface ConfigStorageEvent extends Event {

  /**
   * Retrieves the type why this event has been fired.
   *
   * @return The non-null type of this event
   */
  Type getType();

  /**
   * Retrieves the config that has been written to/read from the {@link ConfigStorage}s.
   *
   * @return The non-null config of this event
   */
  ParsedConfig getConfig();

  /**
   * An enumeration of all types that are possible in the {@link ConfigStorageEvent}.
   */
  enum Type {

    /**
     * The type that will be fired on {@link ConfigStorageProvider#read(ParsedConfig)}.
     */
    READ,

    /**
     * The type that will be fired on {@link ConfigStorageProvider#write(ParsedConfig)}.
     */
    WRITE

  }

  /**
   * Factory for the {@link ConfigStorageEvent}.
   */
  @AssistedFactory(ConfigStorageEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ConfigStorageEvent} for the given type and config.
     *
     * @param type   The non-null type of the action in the storage
     * @param config The non-null config that has been written/read
     * @return The new non-null {@link ConfigStorageEvent}
     */
    ConfigStorageEvent create(@Assisted Type type, @Assisted ParsedConfig config);

  }

}
