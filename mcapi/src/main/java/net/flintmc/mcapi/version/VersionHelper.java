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

package net.flintmc.mcapi.version;

/**
 * The version helper can be used to write version specific code in the internal module.
 */
public interface VersionHelper {

  /**
   * Retrieves the major version.
   *
   * @return The major version.
   */
  int getMajor();

  /**
   * Retrieves the minor version.
   *
   * @return The minor version.
   */
  int getMinor();

  /**
   * Retrieves the patch version.
   *
   * @return The patch version.
   */
  int getPatch();

  /**
   * Whether the minor version of the client is below the given minor version.
   *
   * @param minor The minor version.
   * @return {@code true} if the minor version of the client is below the given minor version,
   * otherwise {@code false}.
   */
  boolean isUnder(int minor);

  /**
   * Whether the minor and patch versions of the client is below the given minor and patch
   * versions.
   *
   * @param minor The minor version.
   * @param patch The patch version.
   * @return {@code true} if the minor and patch versions of the client is below the given minor and
   * patch versions, otherwise {@code false}.
   */
  boolean isUnder(int minor, int patch);

  /**
   * Whether the versioning of the client is below the given versioning.
   *
   * @param major The major version.
   * @param minor The minor version.
   * @param patch The patch version.
   * @return {@code true} if the client version under the given version numbers, otherwise {@code
   * false}.
   */
  boolean isUnder(int major, int minor, int patch);

  /**
   * Retrieves the current game version.
   *
   * @return The current game version.
   */
  String getVersion();
}
