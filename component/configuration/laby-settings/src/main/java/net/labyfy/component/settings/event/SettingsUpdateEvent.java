package net.labyfy.component.settings.event;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.settings.registered.RegisteredSetting;

public interface SettingsUpdateEvent {

  RegisteredSetting getSetting();

  @AssistedFactory(SettingsUpdateEvent.class)
  interface Factory {

    SettingsUpdateEvent create(@Assisted RegisteredSetting setting);

  }

}
