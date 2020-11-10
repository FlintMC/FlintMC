package net.flintmc.mcapi.internal.settings.flint.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.settings.flint.event.SettingsUpdateEvent;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

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
