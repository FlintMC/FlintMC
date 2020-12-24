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

package net.flintmc.util.mappings.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * I/O utility class for mapping parser.
 */
public final class IOUtils {

  private IOUtils() {
  }

  /**
   * Read an input stream to string.
   *
   * @param inputStream The non-null input stream to be read
   * @return A string based on the input stream data.
   * @throws IOException If the input stream cannot be read.
   */
  public static String readToString(InputStream inputStream) throws IOException {
    StringBuilder builder = new StringBuilder();
    byte[] buffer = new byte[4096];
    int length;

    while ((length = inputStream.read(buffer)) > 0) {
      builder.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
    }

    inputStream.close();
    return builder.toString();
  }

  /**
   * Reads an input stream into a byte array.
   *
   * @param inputStream The non-null input stream to be read
   * @return The new byte array out of the input stream
   * @throws IOException If the input stream cannot be read
   */
  public static byte[] readToArray(InputStream inputStream) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    byte[] buffer = new byte[4096];
    int length;

    while ((length = inputStream.read(buffer)) > 0) {
      outputStream.write(buffer, 0, length);
    }

    inputStream.close();
    return outputStream.toByteArray();
  }
}
