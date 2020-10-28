package net.labyfy.component.config.event;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.eventbus.event.Event;
import net.labyfy.component.inject.assisted.AssistedFactory;

// fired only in POST phase
public interface ConfigDiscoveredEvent extends Event {

  ParsedConfig getConfig();

  @AssistedFactory(ConfigDiscoveredEvent.class)
  interface Factory {

    ConfigDiscoveredEvent create(@Assisted ParsedConfig config);

  }

}
