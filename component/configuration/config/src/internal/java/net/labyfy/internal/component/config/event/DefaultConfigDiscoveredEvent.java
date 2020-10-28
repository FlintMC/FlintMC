package net.labyfy.internal.component.config.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.config.event.ConfigDiscoveredEvent;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.inject.implement.Implement;

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
