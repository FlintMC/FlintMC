package net.flintmc.framework.config.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * This event will be fired after a {@link Config} has been discovered, completely generated and an instance is
 * available.
 * <p>
 * It will only be fired in the {@link Subscribe.Phase#POST} phase.
 *
 * @see Subscribe
 */
public interface ConfigDiscoveredEvent extends Event {

  /**
   * Retrieves the config that has been generated.
   *
   * @return The non-null config
   */
  ParsedConfig getConfig();

  /**
   * Factory for the {@link ConfigDiscoveredEvent}.
   */
  @AssistedFactory(ConfigDiscoveredEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ConfigDiscoveredEvent} for the given config.
     *
     * @param config The non-null config that has been generated
     * @return The new non-null {@link ConfigDiscoveredEvent}
     */
    ConfigDiscoveredEvent create(@Assisted ParsedConfig config);

  }

}
