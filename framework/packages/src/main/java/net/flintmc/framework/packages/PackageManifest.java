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

/**
 * Represents the metadata of a package.
 */
public interface PackageManifest {

  /**
   * Retrieves the name of the package.
   *
   * @return The name of the package
   */
  String getName();

  /**
   * Retrieves the group of the package.
   *
   * @return The group of the package
   */
  String getGroup();

  /**
   * Retrieves the display name of the package. This will always be a human
   * readable string.
   *
   * @return The display name of the package
   */
  String getDisplayName();

  /**
   * Retrieves the version of the package.
   *
   * @return The version of the package
   */
  String getVersion();

  /**
   * Retrieves the parent package.
   *
   * @return The parent package, or {@code null}, if none
   */
  String getParent();

  /**
   * Determines whether the package containing this manifest is considered a meta package.
   *
   * @return Whether this package is a meta package
   */
  boolean isMetaPackage();

  /**
   * Retrieves tha version of the package.
   *
   * @return the semantic version of the package
   */
  SemanticVersion getSemanticVersion();

  /**
   * Retrieves the list of authors of this package.
   *
   * @return The list of authors of this package
   */
  Set<String> getAuthors();

  /**
   * @return a set of library/asset paths that should be available in the
   * package's classpath at runtime.
   */
  Set<String> getRuntimeClassPath();

  /**
   * Retrieves the description of the package. This will always be a human
   * readable string.
   *
   * @return The description of the package
   */
  String getDescription();

  /**
   * The list of dependencies this package has. This includes `hard` and `soft`
   * dependencies.
   *
   * @return The list of dependencies this package has
   */
  Set<? extends DependencyDescription> getDependencies();

  /**
   * Checks if this package manifest is valid, e.g. if all necessary values are
   * present.
   *
   * @return {@code true} if the manifest is valid, {@code false} otherwise
   */
  boolean isValid();

  /**
   * Adds a dependency at runtime. The dependency will only be resolved if this
   * happens before the {@link PackageLoader} loads the package.
   *
   * @param packageName the name of the {@link Package} to depend on
   * @param versions    the version to depend on
   * @param channel     the channel to depend on
   */
  void addDependency(String packageName, String versions, String channel);
}
