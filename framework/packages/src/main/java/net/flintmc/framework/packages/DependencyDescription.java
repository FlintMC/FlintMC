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

import java.util.List;

/** Represents a dependency a package may have. */
public interface DependencyDescription {

  /**
   * Retrieves the name of the package this dependency refers to.
   *
   * @return The name of the package this dependency refers to
   */
  String getName();

  /**
   * Retrieves the list of versions satisfying the dependency constraint.
   *
   * @return The list of versions satisfying the dependency constraint
   */
  List<String> getVersions();

  /** @return the repository channel this dependency was published in. */
  String getChannel();

  /**
   * Checks if a package satisfies this dependency.
   *
   * @param manifest The {@link PackageManifest} of the package to check
   * @return {@code true}, if the package satisfies this dependency, {@code false} otherwise
   */
  boolean matches(PackageManifest manifest);
}
