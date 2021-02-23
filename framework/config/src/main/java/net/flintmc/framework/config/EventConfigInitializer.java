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

package net.flintmc.framework.config;

import net.flintmc.framework.config.annotation.ConfigInit;
import net.flintmc.framework.config.generator.ParsedConfig;

/**
 * Listens for events and initializes {@link ParsedConfig}s based on their {@link ConfigInit}
 * annotation.
 */
public interface EventConfigInitializer {

  /**
   * Registers a {@link ParsedConfig} for later, event-dependant initialization.
   *
   * @param config     The config which will be initialized later
   * @param configInit The initialization properties, containing event class and phase
   */
  void registerPendingInitialization(ParsedConfig config, ConfigInit configInit);
}
