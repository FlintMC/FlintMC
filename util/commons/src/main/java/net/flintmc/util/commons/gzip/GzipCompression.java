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

package net.flintmc.util.commons.gzip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipCompression {

  /**
   * Compresses a given input with the gzip compression algorithm.
   *
   * @param input the input to compress
   * @return the compressed data
   */
  public static byte[] compress(byte[] input) {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
      GZIPOutputStream gzip = new GZIPOutputStream(bos);
      gzip.write(input);
      gzip.close();
      byte[] compressed = bos.toByteArray();
      bos.close();
      return compressed;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return input;
  }

  /**
   * Decompress a given input with the gzip compression algorithm.
   *
   * @param input the input to decompress
   * @return the decompressed data
   */
  public static byte[] decompress(byte[] input) {
    try {
      byte[] buffer = new byte[1024];
      ByteArrayInputStream bis = new ByteArrayInputStream(input);
      GZIPInputStream gis = new GZIPInputStream(bis);
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      int len;
      while ((len = gis.read(buffer)) > 0) {
        out.write(buffer, 0, len);
      }

      gis.close();
      out.close();
      return out.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return input;
  }
}
