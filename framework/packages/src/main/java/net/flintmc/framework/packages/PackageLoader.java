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

package net.flintmc.framework.packages;

import java.util.Set;

/** Represents the service responsible for loading packages. */
public interface PackageLoader {

  String PACKAGE_DIR = "flint/packages";
  String PACKAGE_DIR_TEMPLATE = "${FLINT_PACKAGE_DIR}";

  String LIBRARY_DIR = "libraries";
  String LIBRARY_DIR_TEMPLATE = "${FLINT_LIBRARY_DIR}";

  /**
   * Retrieves a set of all packages. This ignores the {@link PackageState} completely, which means
   * this set may for example also contain errored or not loaded packages.
   *
   * @return A set of all packages found by the loader
   */
  Set<Package> getAllPackages();

  /**
   * Retrieves a set of all packages which are in the {@link PackageState#LOADED} or {@link
   * PackageState#ENABLED} state.
   *
   * @return A set of all loaded or enabled packages
   */
  Set<Package> getLoadedPackages();

  void load();
}
