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

/**
 * Provider for all known filesystem implementations.
 */
public interface WebFileSystemService {

  /**
   * Retrieves the filesystem implementation by its protocol name lazy. Creates the instance via the
   * injection holder if it hasn't been created yet.
   *
   * @param key the filesystem protocol name
   * @return the retrieved filesystem
   * @throws UnknownWebFileSystemException if no filesystem could be found
   */
  WebFileSystemHandler getHandlerFor(String key) throws UnknownWebFileSystemException;
}
