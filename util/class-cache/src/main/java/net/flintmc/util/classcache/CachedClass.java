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

package net.flintmc.util.classcache;

import net.flintmc.framework.inject.assisted.Assisted;
import java.util.UUID;

/**
 * Represents a class that may or may not be cached.
 */
public interface CachedClass {

  /**
   * The template path to a cached class binary file. "{UUID}" should be
   * replaced with a unique name for this file.
   */
  String CACHED_CLASS_PATH = "flint/class-cache/{UUID}.bin";

  /**
   * Reads the entire cached class. Performs blocking IO.
   *
   * @return the bytes that are in the file or null if the file doesn't exist
   */
  byte[] read();

  /**
   * Writes the given bytes into the file for this class (and creates the file
   * if it doesn't exist yet). Performs blocking IO.
   *
   * @param bytecode the bytes to write
   */
  void write(byte[] bytecode);

  /**
   * Retrieves the {@link UUID} for this cached class. This UUID is unique not
   * only to this exact transformation state of the represented class.
   *
   * @return the {@link UUID} of this class
   */
  UUID getUUID();

  /**
   * Retrieves the name of this cached class.
   *
   * @return the name of the class
   */
  String getName();

  /**
   * Checks whether the file for this cached class exists and is not empty.
   *
   * @return true, if there is cached data available
   */
  boolean hasBytecode();


  /**
   * Injectable factory to construct {@link CachedClass} instances.
   */
  interface Factory {

    /**
     * Creates a new {@link CachedClass} instance.
     *
     * @param name the name of the class
     * @param uuid the {@link UUID} of the cached class
     * @return a new {@link CachedClass} instance that represents the cached
     * state
     */
    CachedClass create(@Assisted("name") String name,
        @Assisted("uuid") UUID uuid);

  }

}
