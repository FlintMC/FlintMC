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

package net.flintmc.mcapi.nbt.io.write;

import java.io.DataOutputStream;
import java.io.IOException;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.nbt.NBT;

/**
 * A data output stream lets an application write primitive Java data types and named binary tags to
 * an output stream in a portable way. An application can then use a data input stream to read the
 * data back in.
 */
public interface NBTDataOutputStream {

  /**
   * Writes a full tag to this output stream.
   *
   * <p>(ID, name and value when applicable)
   *
   * @param name The name from the tag.
   * @param nbt  The named binary tag.
   * @throws IOException If an error occurred during writing.
   */
  void writeFullyFormedTag(String name, NBT nbt) throws IOException;

  /**
   * Writes a TAG_End to this output stream.
   *
   * @throws IOException If an error occurred during writing.
   */
  void writeEndTag() throws IOException;

  /**
   * Retrieves a data output stream.
   *
   * @return A data output stream.
   */
  DataOutputStream getDataOutputStream();

  /**
   * A factory class for the {@link NBTDataOutputStream}.
   */
  @AssistedFactory(NBTDataOutputStream.class)
  interface Factory {

    /**
     * Creates a new {@link NBTDataOutputStream} with the given {@link DataOutputStream}.
     *
     * @param dataOutputStream The output stream for the named binary tag.
     * @return A created output stream.
     */
    NBTDataOutputStream create(@Assisted("dataOutputStream") DataOutputStream dataOutputStream);
  }
}
