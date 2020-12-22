package net.flintmc.mcapi.settings.flint.event;

import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

/**
 * Fired whenever a setting gets updated via {@link ConfigObjectReference#setValue(Object)} or
 * {@link RegisteredSetting#setEnabled(boolean)}. This event will be fired in both the {@link
 * Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phases.
 *
 * @see Subscribe
 */
public interface SettingUpdateEvent extends Event {

  /**
   * Retrieves the setting that has been updated in this event.
   *
   * @return The non-null updated setting
   */
  RegisteredSetting getSetting();

  /**
   * Factory for the {@link SettingUpdateEvent}.
   */
  @AssistedFactory(SettingUpdateEvent.class)
  interface Factory {

    /**
     * Creates a new {@link SettingUpdateEvent} with the given setting.
     *
     * @param setting The non-null setting that has been updated
     * @return The new non-null event
     */
    SettingUpdateEvent create(@Assisted RegisteredSetting setting);
  }
}
