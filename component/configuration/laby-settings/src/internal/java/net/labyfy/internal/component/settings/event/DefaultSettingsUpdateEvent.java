package net.labyfy.internal.component.settings.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.settings.event.SettingsUpdateEvent;
import net.labyfy.component.settings.registered.RegisteredSetting;

@Implement(SettingsUpdateEvent.class)
public class DefaultSettingsUpdateEvent implements SettingsUpdateEvent {

  private final RegisteredSetting setting;

  @AssistedInject
  public DefaultSettingsUpdateEvent(@Assisted RegisteredSetting setting) {
    this.setting = setting;
  }

  @Override
  public RegisteredSetting getSetting() {
    return this.setting;
  }
}
