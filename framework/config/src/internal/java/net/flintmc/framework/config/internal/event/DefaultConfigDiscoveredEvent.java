package net.flintmc.framework.config.internal.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.config.event.ConfigDiscoveredEvent;
import net.flintmc.framework.config.generator.ParsedConfig;
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
