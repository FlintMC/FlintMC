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

package net.flintmc.framework.config.event;

import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * This event will be fired after a {@link Config} has been discovered, completely generated and an
 * instance is available.
 * <p>
 * It will only be fired in the {@link Subscribe.Phase#POST} phase.
 *
 * @see Subscribe
 */
@Subscribable(Phase.POST)
public interface ConfigDiscoveredEvent extends Event {

  /**
   * Retrieves the config that has been generated.
   *
   * @return The non-null config
   */
  ParsedConfig getConfig();

  /**
   * Factory for the {@link ConfigDiscoveredEvent}.
   */
  @AssistedFactory(ConfigDiscoveredEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ConfigDiscoveredEvent} for the given config.
     *
     * @param config The non-null config that has been generated
     * @return The new non-null {@link ConfigDiscoveredEvent}
     */
    ConfigDiscoveredEvent create(@Assisted ParsedConfig config);

  }

}
