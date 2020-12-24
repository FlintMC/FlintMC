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

package net.flintmc.framework.packages.internal.source;

import net.flintmc.framework.packages.Package;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

/**
 * Represents a location a package can be access with. Currently package sources are considered to
 * be an implementation detail.
 */
public interface PackageSource extends AutoCloseable {
  /**
   * Creates a package source matching the given package.
   *
   * @param pkg The package to create the source for
   * @return The created source which can be used to access resources of the package
   * @throws IOException If the jar file could not be read.
   */
  static PackageSource of(Package pkg) throws IOException {
    if (pkg.getFile() == null) {
      return new ClasspathSource();
    } else {
      return new FileSource(pkg.getFile());
    }
  }

  /** {@inheritDoc} */
  @Override
  default void close() throws IOException {}

  /**
   * Locates a resource within the resources accessible with this source.
   *
   * @param path The path of the resource to find
   * @return An {@link URL} pointing to the found resource, or null if not found
   */
  URL findResource(String path);

  /**
   * Locates all resources matching the given name accessible with this source.
   *
   * @param name The name of the resources to find
   * @return An enumeration of all resources matching the given name accessible with this source
   * @throws IOException If an I/O error occurred while finding the resources
   */
  Enumeration<URL> findResources(String name) throws IOException;

  /**
   * Locates all resource accessible with this source.
   *
   * @return An enumeration of all resources accessible with this source
   * @throws IOException If an I/O error occurred while locating the resources
   * @throws URISyntaxException If an URISyntaxException occurred while locating the resource.
   */
  Enumeration<URL> findAllResources() throws IOException, URISyntaxException;
}
