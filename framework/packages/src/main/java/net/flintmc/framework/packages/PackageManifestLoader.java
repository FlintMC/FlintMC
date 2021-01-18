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

import java.io.IOException;
import java.net.URL;
import java.util.jar.JarFile;

/**
 * Responsible for reading the package manifest out of a given jar file.
 */
public interface PackageManifestLoader {

  /**
   * The name of the manifest file.
   */
  String MANIFEST_NAME = "manifest.json";

  /**
   * Checks if a package.json manifest is present it the given JarFile, but
   * doesn not guarantee that it contains a valid package manifest.
   *
   * @param file The JarFile to check for a manifest
   * @return {@code true} if a manifest is present, {@code false} otherwise
   */
  boolean isManifestPresent(JarFile file);

  /**
   * Tries to load the package manifest from the available manifest file in the
   * given jar. Will fail, if the manifest is not present or readable. This
   * method does not check if the manifests content is valid, use the {@link
   * PackageManifest#isValid()} method for that.
   *
   * @param file The JarFile in which the manifest can be found.
   * @return A loaded package manifest.
   * @throws IOException If the file could not be read.
   */
  PackageManifest loadManifest(JarFile file) throws IOException;

  /**
   * Tries to load the package manifest from the available manifest file at the
   * given url. Will fail, if the manifest is not present or readable. This
   * method does not check if the manifests content is valid, use the {@link
   * PackageManifest#isValid()} method for that.
   *
   * @param url The url at which the manifest can be found.
   * @return A loaded package manifest.
   * @throws IOException If the file could not be read.
   */
  PackageManifest loadManifest(URL url) throws IOException;
}
