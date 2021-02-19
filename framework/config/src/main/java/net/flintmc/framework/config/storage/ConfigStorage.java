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

package net.flintmc.framework.config.storage;

import net.flintmc.framework.config.generator.ParsedConfig;

/**
 * A storage to store {@link ParsedConfig}s and read them again.
 *
 * @see StoragePriority
 */
public interface ConfigStorage {

  /**
   * Retrieves the name of this storage, unique per {@link ConfigStorageProvider}.
   *
   * @return The non-null name of this storage
   */
  String getName();

  /**
   * Instantly stores the given config (overrides if the config has already been stored).
   *
   * @param config The non-null config to be stored
   */
  void write(ParsedConfig config);

  /**
   * Instantly reads the given config, if the config has never been stored to this storage, nothing
   * will happen.
   *
   * @param config The non-null config to be read
   */
  void read(ParsedConfig config);

}
