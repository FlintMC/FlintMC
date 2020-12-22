package net.flintmc.framework.config.internal.event;

import net.flintmc.framework.config.event.ConfigStorageEvent;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;

@Implement(ConfigStorageEvent.class)
public class DefaultConfigStorageEvent implements ConfigStorageEvent {

  private final Type type;
  private final ParsedConfig config;

  @AssistedInject
  public DefaultConfigStorageEvent(@Assisted Type type, @Assisted ParsedConfig config) {
    this.type = type;
    this.config = config;
  }

  /** {@inheritDoc} */
  @Override
  public Type getType() {
    return this.type;
  }

  /** {@inheritDoc} */
  @Override
  public ParsedConfig getConfig() {
    return this.config;
  }
}
