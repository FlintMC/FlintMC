package net.labyfy.internal.component.config.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.config.event.ConfigStorageEvent;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.inject.implement.Implement;

@Implement(ConfigStorageEvent.class)
public class DefaultConfigStorageEvent implements ConfigStorageEvent {

  private final Type type;
  private final ParsedConfig config;

  @AssistedInject
  public DefaultConfigStorageEvent(@Assisted Type type, @Assisted ParsedConfig config) {
    this.type = type;
    this.config = config;
  }

  @Override
  public Type getType() {
    return this.type;
  }

  @Override
  public ParsedConfig getConfig() {
    return this.config;
  }
}
