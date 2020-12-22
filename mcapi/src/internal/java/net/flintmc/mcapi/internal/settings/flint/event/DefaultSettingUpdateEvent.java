package net.flintmc.mcapi.internal.settings.flint.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.settings.flint.event.SettingUpdateEvent;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

@Implement(SettingUpdateEvent.class)
public class DefaultSettingUpdateEvent implements SettingUpdateEvent {

  private final RegisteredSetting setting;

  @AssistedInject
  public DefaultSettingUpdateEvent(@Assisted RegisteredSetting setting) {
    this.setting = setting;
  }

  /** {@inheritDoc} */
  @Override
  public RegisteredSetting getSetting() {
    return this.setting;
  }
}
