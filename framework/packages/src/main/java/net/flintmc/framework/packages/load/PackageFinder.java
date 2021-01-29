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

package net.flintmc.framework.packages.load;

import java.io.IOException;
import java.util.List;
import net.flintmc.framework.packages.Package;

/**
 * Utility to find potentially loadable packages.
 */
public interface PackageFinder {

  /**
   * Finds packages in a given directory. Creates the directory if it doesn't
   * exist.
   *
   * @param path the path of the directory to search packages in
   * @return a {@link List} of {@link Package}s.
   * @throws IOException if the files coulnd't be read of the package directory
   *                     doesn't exist and can't be created.
   */
  List<Package> findPackages(String path) throws IOException;

  /**
   * Finds packages that are already loaded into classpath and creates
   * appropriate representations for them.
   *
   * @return a list of created {@link Package} instances
   */
  List<Package> findPackagesInClasspath();

}
