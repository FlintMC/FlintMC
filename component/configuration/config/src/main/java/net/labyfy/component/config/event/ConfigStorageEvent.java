package net.labyfy.component.config.event;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.eventbus.event.Event;
import net.labyfy.component.inject.assisted.AssistedFactory;

public interface ConfigStorageEvent extends Event {

  Type getType();

  ParsedConfig getConfig();

  enum Type {

    READ,
    WRITE

  }

  @AssistedFactory(ConfigStorageEvent.class)
  interface Factory {

    ConfigStorageEvent create(@Assisted Type type, @Assisted ParsedConfig config);

  }

}
