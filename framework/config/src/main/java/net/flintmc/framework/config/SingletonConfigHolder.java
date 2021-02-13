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

import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.annotation.MultiInstanceConfig;
import net.flintmc.framework.config.generator.ParsedConfig;
import java.util.Collection;

/**
 * This class contains every {@link Config} that is not annotated with {@link MultiInstanceConfig}.
 */
public interface SingletonConfigHolder {

  /**
   * Retrieves the singleton instance of a config, this is only available for interfaces annotated
   * with {@link Config} and not with {@link MultiInstanceConfig} or for configs manually registered
   * via {@link #registerSingletonConfig(ParsedConfig)}.
   *
   * @param configClass The non-null class of the config to be retrieved
   * @return The config or {@code null} if no singleton config for the given class is available
   */
  ParsedConfig getSingletonConfig(Class<? extends ParsedConfig> configClass);

  /**
   * Retrieves a collection of all registered singleton configs.
   *
   * @return A non-null immutable collection of all singleton configs
   * @see #getSingletonConfig(Class)
   */
  Collection<ParsedConfig> getSingletonConfigs();

  /**
   * Registers a new singleton config.
   *
   * @param config The non-null config to be registered
   * @see #getSingletonConfig(Class)
   */
  void registerSingletonConfig(ParsedConfig config);

}
