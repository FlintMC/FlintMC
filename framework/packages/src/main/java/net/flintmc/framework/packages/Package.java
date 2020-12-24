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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.packages.localization.PackageLocalizationLoader;

import java.io.File;
import java.util.jar.JarFile;

/** Represents a package which has been identified and possibly loaded. */
public interface Package {
  /**
   * Retrieves the manifest of this package.
   *
   * @return The package manifest of this package, or null, if no manifest is present
   */
  PackageManifest getPackageManifest();

  /**
   * Retrieves the name of this package. This method differs from {@link PackageManifest#getName()}
   * as in that it falls back to the name of the file if no manifest is present.
   *
   * @return The raw name of this package
   */
  String getName();

  /**
   * Retrieves the display name of this package. This method differs from {@link
   * PackageManifest#getDisplayName()} as in that it falls back to the name of the file if no
   * manifest is present.
   *
   * @return The name of this package as it should be displayed to the user
   */
  String getDisplayName();

  /**
   * Retrieves the version of this package. This method differs from {@link
   * PackageManifest#getVersion()} as in that it falls back to {@code "unknown"} if no manifest is
   * present.
   *
   * @return The version of this package, or {@code "unknown"}
   */
  String getVersion();

  /**
   * Retrieves the state of this package indicating if the package has been loaded successfully.
   *
   * @return The current state of this package
   */
  PackageState getState();

  /**
   * Sets the state of the package. The previous state must be {@link PackageState#NOT_LOADED} and
   * the state can't be explicitly set to {@link PackageState#LOADED}.To set the state to {@link
   * PackageState#LOADED} call {@link #load()}.
   *
   * @param state The new state of the package
   * @throws IllegalArgumentException If the package state is not {@link PackageState#NOT_LOADED} or
   *     if the new state is {@link PackageState#LOADED}
   */
  void setState(PackageState state);

  /**
   * Retrieves the file of the package if it has been loaded from one.
   *
   * @return The file this package is loaded from, or null, if the package has been loaded from the
   *     classpath
   */
  File getFile();

  /**
   * Tries to load this package. Package must be {@link PackageState#NOT_LOADED}. This call should
   * not throw exceptions caused by a failed load attempt but only exceptions caused by unmet
   * preconditions (e.g. wrong state).
   *
   * @return The state of the package after the load attempt
   * @throws IllegalStateException If the package state is not {@link PackageState#NOT_LOADED}
   */
  PackageState load();

  /**
   * Tries to enable this package by parsing annotations in autoload classes. If an exception occurs
   * the package may stay loaded and become partially enabled.
   */
  void enable();

  /**
   * Retrieves the class loader the package has been loaded with. In order to call this method the
   * package has to be in {@link PackageState#LOADED} or {@link PackageState#ENABLED} state.
   *
   * @return The class loader used for loading this package
   * @throws IllegalStateException If the package is not in the {@link PackageState#LOADED} or
   *     {@link PackageState#ENABLED} state
   */
  PackageClassLoader getPackageClassLoader();

  /**
   * Retrieves the localization loader the package has been loaded with.
   *
   * @return The localization loader used for loading the translations.
   */
  PackageLocalizationLoader getPackageLocalizationLoader();

  /**
   * Retrieves the exception which occurred while loading the package. In order to call this method
   * the package has to be in the {@link PackageState#ERRORED} state.
   *
   * @return The exception that caused loading of this package to error
   * @throws IllegalStateException If the pakcage is not in the {@link PackageState#ERRORED} state
   */
  Exception getLoadException();

  @AssistedFactory(Package.class)
  interface Factory {

    /**
     * Creates a Package instance.
     *
     * @param file The File of the Package
     * @param jarFile the JarFile of the Package
     * @return a Package instance representing the Package
     */
    Package create(@Assisted File file, @Assisted JarFile jarFile);
  }
}
