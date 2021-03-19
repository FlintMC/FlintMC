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

/**
 * Index for class cache that maps class names and cache identifiers to files on
 * disk.
 */
public interface ClassCacheIndex {

  /**
   * Path to the index file.
   */
  String INDEX_FILE_PATH = "flint/class-cache/index.json";

  /**
   * Retrieves the latest cache identifier fo a given name or null if no data
   * has been cached for that name.
   *
   * @param name the name of the class
   * @return the cache identifier for the latest data
   */
  long getLatestId(String name);

  /**
   * Retrieves a {@link CachedClass} instance for the given name and identifier
   * that represents the cache for those values. This is not an indication for
   * whether cached data is actually available (see {@link
   * CachedClass#hasBytecode()}). Adds the {@link CachedClass} to the index if
   * not already in it.
   *
   * @param name the name of the class
   * @param id   the cache identifier for the state of the bytecode
   * @return a {@link CachedClass} instance representing the desired state
   */
  CachedClass getCachedClass(String name, long id);

  /**
   * Writes the index to the disk.
   */
  void write();

}
