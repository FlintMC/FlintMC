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

package net.flintmc.render.gui.webgui;

import java.io.FileNotFoundException;

/**
 * Represents a web file system that can be used by the web front-end.
 */
public interface WebFileSystemHandler {

  /**
   * Check if file path exists.
   *
   * @param path The path to check for a file at
   * @return {@code true} if the file exists, {@code false} otherwise
   */
  boolean existsFile(String path);

  /**
   * Gets a {@link WebResource}.
   *
   * @param path The path to the resource
   * @return the resource
   * @throws FileNotFoundException if no resource was found at the given path
   */
  WebResource getFile(String path) throws FileNotFoundException;
}
