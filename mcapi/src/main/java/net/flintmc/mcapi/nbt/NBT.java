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

package net.flintmc.mcapi.nbt;

import java.io.IOException;
import net.flintmc.mcapi.nbt.io.read.NBTDataInputStream;
import net.flintmc.mcapi.nbt.io.write.NBTDataOutputStream;

/**
 * The basic representation of a NBT tag.
 */
public interface NBT {

  /**
   * Retrieves the identifier of this tag type.
   *
   * @return The identifier of this tag type.
   */
  NBTType getIdentifier();

  /**
   * Reads the contents of the tag from the given source. The tag {@link #getIdentifier()} is
   * supposed to be already read.
   *
   * <p>For {@link NBTList}, it assumes the subtag type identifier as already been read.
   *
   * @param inputStream The input stream to read.
   * @throws IOException If an error occurred during reading
   */
  void readContents(NBTDataInputStream inputStream) throws IOException;

  /**
   * Writes the contents of the tag to the given destination. The tag {@link #getIdentifier()} is
   * supposed to be already written.
   *
   * @param outputStream The output stream to write.
   * @throws IOException If an error occurred during writing.
   */
  void writeContents(NBTDataOutputStream outputStream) throws IOException;

  /**
   * Retrieves the named binary tag content as a {@link String}
   *
   * @return The content of a named binary tag as a string.
   */
  String asString();
}
