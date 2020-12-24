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

package net.flintmc.framework.packages.localization;

import java.io.IOException;
import java.util.Map;
import java.util.jar.JarFile;

/** Represents a loader for {@link PackageLocalization}'s. */
public interface PackageLocalizationLoader {

  /**
   * Whether a language folder is present in the given {@link JarFile}.
   *
   * @param file The jar file to check for the language folder.
   * @return {@code true} if a language folder is present, otherwise {@code false}.
   */
  boolean isLanguageFolderPresent(JarFile file);

  /**
   * Tries to load all available languages from the folder in the given jar.
   *
   * @param file The jar file in which the languages can be found.
   * @throws IOException If the files could not be read.
   */
  void loadLocalizations(JarFile file) throws IOException;

  /**
   * Retrieves a key-value system with all available languages.
   *
   * @return A key-value system with all available languages.
   */
  Map<String, PackageLocalization> getLocalizations();
}
