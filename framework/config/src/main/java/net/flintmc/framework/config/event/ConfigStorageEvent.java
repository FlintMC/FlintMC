package net.flintmc.framework.config.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.storage.ConfigStorage;
import net.flintmc.framework.config.storage.ConfigStorageProvider;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.AssistedFactory;

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
