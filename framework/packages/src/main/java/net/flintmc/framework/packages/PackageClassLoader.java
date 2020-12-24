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

/**
 * Represents a {@link ClassLoader} used for loading a package. Note that you should never cast
 * instances of this interface to {@link ClassLoader}, but rather use the {@link #asClassLoader()}
 * method.
 */
public interface PackageClassLoader {
  /**
   * Tries to find a class present in the package.
   *
   * @param name The name of the class to find
   * @return The found class instance
   * @throws ClassNotFoundException If the class could not be found in the package
   */
  Class<?> findClass(String name) throws ClassNotFoundException;

  /**
   * Unwraps the plain Java ClassLoader reference that is used for defining classes within the
   * package owning this class loader.
   *
   * @return The {@link ClassLoader} representation of this {@link PackageClassLoader}
   */
  ClassLoader asClassLoader();

  /**
   * Retrieves the package owning this class loader.
   *
   * @return The package owning this class loader
   */
  Package getOwner();
}
