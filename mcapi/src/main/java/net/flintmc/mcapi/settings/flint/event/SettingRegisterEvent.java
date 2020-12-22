package net.flintmc.mcapi.settings.flint.event;

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.registered.SettingsProvider;

/**
 * Fired whenever a setting is registered via {@link
 * SettingsProvider#registerSetting(RegisteredSetting)}. This event will be fired in both the {@link
 * Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phases, but cancellation will only have an
 * effect in the {@link Phase#PRE} phase. It will not be fired for each sub setting of a setting,
 * but in the {@link Phase#PRE} phase, the given setting won't contain any sub settings, those will
 * be added before the {@link Phase#POST} phase.
 *
 * @see Subscribe
 */
public interface SettingRegisterEvent extends Event, Cancellable {

  /**
   * Retrieves the setting that has been registered in this event.
   *
   * @return The non-null updated setting
   */
  RegisteredSetting getSetting();

  /** Factory for the {@link SettingRegisterEvent}. */
  @AssistedFactory(SettingRegisterEvent.class)
  interface Factory {

    /**
     * Creates a new {@link SettingRegisterEvent} with the given setting.
     *
     * @param setting The non-null setting that has been registered
     * @return The new non-null event
     */
    SettingRegisterEvent create(@Assisted RegisteredSetting setting);
  }
}
