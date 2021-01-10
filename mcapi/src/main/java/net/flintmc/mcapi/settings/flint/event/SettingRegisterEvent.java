/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.settings.flint.event;

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.registered.SettingsProvider;

/**
 * Fired whenever a setting is registered via {@link SettingsProvider#registerSetting(RegisteredSetting)}.
 * This event will be fired in both the {@link Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST}
 * phases, but cancellation will only have an effect in the {@link Phase#PRE} phase. It will not be
 * fired for each sub setting of a setting, but in the {@link Phase#PRE} phase, the given setting
 * won't contain any sub settings, those will be added before the {@link Phase#POST} phase.
 *
 * @see Subscribe
 */
@Subscribable({Phase.PRE, Phase.POST})
public interface SettingRegisterEvent extends Event, Cancellable {

  /**
   * Retrieves the setting that has been registered in this event.
   *
   * @return The non-null updated setting
   */
  RegisteredSetting getSetting();

  /**
   * Factory for the {@link SettingRegisterEvent}.
   */
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
