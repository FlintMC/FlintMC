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
