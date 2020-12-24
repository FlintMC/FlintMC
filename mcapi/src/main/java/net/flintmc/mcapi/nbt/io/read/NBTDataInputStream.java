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

package net.flintmc.mcapi.nbt.io.read;

import java.io.DataInputStream;
import java.io.IOException;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.nbt.NBT;
import net.flintmc.util.commons.Pair;

/**
 * A data input stream lets an application read primitive Java data types and named binary tags from
 * an underlying input stream in a machine-independent way. An application ues a data output stream
 * to write data that can later be read by a data input stream.
 */
public interface NBTDataInputStream {

  /**
   * Reads a full tag from this input stream.
   *
   * <p>(ID, name and value when applicable)
   *
   * @return The fully formed tag.
   */
  Pair<String, NBT> readFullyFormedTag() throws IOException;

  /**
   * Reads a tag from this input stream.
   *
   * @param identifier The identifier of this named binary tag.
   * @return A new named binary tag.
   */
  NBT readTag(int identifier) throws IOException;

  /**
   * Retrieves a data input stream.
   *
   * @return A data input stream.
   */
  DataInputStream getDataInputStream();

  /**
   * A factory class for the {@link NBTDataInputStream}.
   */
  @AssistedFactory(NBTDataInputStream.class)
  interface Factory {

    /**
     * Creates a new {@link NBTDataInputStream} with the given parameters.
     *
     * @param inputStream The input stream to read.
     * @return A created input stream.
     */
    NBTDataInputStream create(@Assisted("inputStream") DataInputStream inputStream);
  }
}
