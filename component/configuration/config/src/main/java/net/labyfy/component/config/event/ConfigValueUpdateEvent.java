package net.labyfy.component.config.event;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.eventbus.event.Event;
import net.labyfy.component.inject.assisted.AssistedFactory;

import javax.annotation.Nullable;

public interface ConfigValueUpdateEvent extends Event {

  ConfigObjectReference getReference();

  Object getPreviousValue();

  Object getNewValue();

  @AssistedFactory(ConfigValueUpdateEvent.class)
  interface Factory {

    ConfigValueUpdateEvent create(@Assisted ConfigObjectReference reference,
                                  @Assisted("previousValue") @Nullable Object previousValue,
                                  @Assisted("newValue") @Nullable Object newValue);

  }

}
