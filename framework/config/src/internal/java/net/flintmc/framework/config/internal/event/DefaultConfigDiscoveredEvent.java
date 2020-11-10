package net.flintmc.framework.config.internal.event;

import net.flintmc.framework.config.event.ConfigDiscoveredEvent;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;

@Implement(ConfigDiscoveredEvent.class)
public class DefaultConfigDiscoveredEvent implements ConfigDiscoveredEvent {

  private final ParsedConfig config;

  @AssistedInject
  public DefaultConfigDiscoveredEvent(@Assisted ParsedConfig config) {
    this.config = config;
  }

  @Override
  public ParsedConfig getConfig() {
    return this.config;
  }
}
