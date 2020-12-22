package net.flintmc.mcapi.internal.settings.flint.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.settings.flint.event.SettingRegisterEvent;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

@Implement(SettingRegisterEvent.class)
public class DefaultSettingRegisterEvent implements SettingRegisterEvent {

  private final RegisteredSetting setting;
  private boolean cancelled;

  @AssistedInject
  public DefaultSettingRegisterEvent(@Assisted RegisteredSetting setting) {
    this.setting = setting;
  }

  /** {@inheritDoc} */
  @Override
  public RegisteredSetting getSetting() {
    return this.setting;
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
