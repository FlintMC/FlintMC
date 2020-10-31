package net.labyfy.internal.component.config.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.config.event.ConfigValueUpdateEvent;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.inject.implement.Implement;

@Implement(ConfigValueUpdateEvent.class)
public class DefaultConfigValueUpdateEvent implements ConfigValueUpdateEvent {

  private final ConfigObjectReference reference;
  private final Object previousValue;
  private final Object newValue;

  @AssistedInject
  public DefaultConfigValueUpdateEvent(@Assisted ConfigObjectReference reference,
                                       @Assisted("previousValue") Object previousValue,
                                       @Assisted("newValue") Object newValue) {
    this.reference = reference;
    this.previousValue = previousValue;
    this.newValue = newValue;
  }

  @Override
  public ConfigObjectReference getReference() {
    return this.reference;
  }

  @Override
  public Object getPreviousValue() {
    return this.previousValue;
  }

  @Override
  public Object getNewValue() {
    return this.newValue;
  }
}
