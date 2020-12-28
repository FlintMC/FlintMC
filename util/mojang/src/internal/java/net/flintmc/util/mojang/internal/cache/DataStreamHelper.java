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

package net.flintmc.util.mojang.internal.cache;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class DataStreamHelper {

  private DataStreamHelper() {
    throw new UnsupportedOperationException();
  }

  public static void writeString(DataOutput output, String value) throws IOException {
    output.writeShort(value.length());
    output.write(value.getBytes(StandardCharsets.UTF_8));
  }

  public static String readString(DataInput input) throws IOException {
    int length = input.readShort();
    if (length < 0) {
      throw new IllegalArgumentException("Invalid array length: " + length);
    }
    byte[] data = new byte[length];
    input.readFully(data);
    return new String(data, StandardCharsets.UTF_8);
  }
}
