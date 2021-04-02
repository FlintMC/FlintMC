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

package net.flintmc.mcapi.settings.flint.options.data;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.generation.annotation.DataFactory;
import net.flintmc.framework.generation.annotation.TargetDataField;

/**
 * This event will be fired whenever a value in a {@link SettingData} has been updated.
 */
@Subscribable({Phase.PRE, Phase.POST})
public interface SettingDataUpdateEvent extends Event {

  /**
   * Retrieves the data that has been updated.
   *
   * @return The non-null data that has been updated
   */
  @TargetDataField("data")
  SettingData getData();

  /**
   * Factory for the {@link SettingDataUpdateEvent}.
   */
  @DataFactory(SettingDataUpdateEvent.class)
  interface Factory {

    /**
     * Creates a new {@link SettingDataUpdateEvent}.
     *
     * @param data The non-null data that has been updated
     * @return The new non-null event
     */
    SettingDataUpdateEvent create(@TargetDataField("data") SettingData data);
  }
}
