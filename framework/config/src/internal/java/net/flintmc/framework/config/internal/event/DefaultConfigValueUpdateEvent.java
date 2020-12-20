package net.flintmc.framework.config.internal.event;

import net.flintmc.framework.config.event.ConfigValueUpdateEvent;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;

import javax.annotation.Nullable;

@Implement(ConfigValueUpdateEvent.class)
public class DefaultConfigValueUpdateEvent implements ConfigValueUpdateEvent {

  private final ConfigObjectReference reference;
  private final Object previousValue;
  private final Object newValue;

  @AssistedInject
  public DefaultConfigValueUpdateEvent(
      @Assisted ConfigObjectReference reference,
      @Assisted("previousValue") @Nullable Object previousValue,
      @Assisted("newValue") @Nullable Object newValue) {
    this.reference = reference;
    this.previousValue = previousValue;
    this.newValue = newValue;
  }

  /** {@inheritDoc} */
  @Override
  public ConfigObjectReference getReference() {
    return this.reference;
  }

  /** {@inheritDoc} */
  @Override
  public Object getPreviousValue() {
    return this.previousValue;
  }

  /** {@inheritDoc} */
  @Override
  public Object getNewValue() {
    return this.newValue;
  }
}
