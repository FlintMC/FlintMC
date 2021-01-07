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

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Represents a resource readable from a web front-end.
 */
public interface WebResource {

  /**
   * @return the path to this resource
   */
  String getPath();

  /**
   * @return the MIME type of this resource
   */
  String getMimeType();

  /**
   * Opens this resource for reading
   *
   * @throws IOException if the resource couldn't be opened
   */
  void open() throws IOException;

  /**
   * Closes this resource for reading
   *
   * @throws IOException if the resource couldn't be closed
   */
  void close() throws IOException;

  /**
   * @return the size of this resource's content in bytes
   * @throws IOException if the size couldn't be obtained
   */
  long getSize() throws IOException;

  /**
   * Reads from this resource
   *
   * @param data   the buffer to write the resource content to
   * @param length the maximum number of bytes to read
   * @return the number of bytes read (and written to data)
   * @throws IOException if the resource couldn't be read
   */
  long readFromFile(ByteBuffer data, long length) throws IOException;
}
