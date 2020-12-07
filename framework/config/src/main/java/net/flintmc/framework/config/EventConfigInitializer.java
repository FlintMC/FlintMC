package net.flintmc.framework.config;

import net.flintmc.framework.config.annotation.ConfigInit;
import net.flintmc.framework.config.generator.ParsedConfig;

/**
 * Listens for events and initializes {@link ParsedConfig}s based on their {@link ConfigInit}
 * annotation.
 */
public interface EventConfigInitializer {

  /**
   * Registers a {@link ParsedConfig} for later, event-dependant initialization.
   *
   * @param config the config which will be initialized later
   * @param configInit the initialization properties, containing event class and phase
   */
  void registerPendingInitialization(ParsedConfig config, ConfigInit configInit);
}
