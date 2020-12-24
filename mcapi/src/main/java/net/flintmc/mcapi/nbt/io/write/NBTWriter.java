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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.nbt.NBT;

/**
 * Writes NBT data to a given destination. Once the output is passed to a {@link NBTWriter}, use
 * {@link #close()} to close the stream.
 */
public interface NBTWriter {

  /**
   * Writes a tag with a name inside the destination.
   *
   * @param name The name to write.
   * @param tag  The tag to write.
   */
  void writeNamed(String name, NBT tag) throws IOException;

  /**
   * Writes a tag contents directly to the destination.
   *
   * @param tag The tag to write.
   */
  void writeRaw(NBT tag) throws IOException;

  /**
   * Closes the writer.
   */
  void close() throws IOException;

  /**
   * A factory class for the {@link NBTWriter}.
   */
  @AssistedFactory(NBTWriter.class)
  interface Factory {

    /**
     * Creates a new {@link NBTWriter} with the given parameters.
     *
     * @param outputStream The output stream for the datas.
     * @param compressed   {@code true} if the data should be compressed, otherwise {@code false}.
     * @return A created named binary tag writer.
     */
    NBTWriter create(
        @Assisted("output") OutputStream outputStream, @Assisted("compressed") boolean compressed);

    /**
     * Creates a new {@link NBTWriter} with the given parameters.
     *
     * @param file       The output for the datas.
     * @param compressed {@code true} if the data should be compressed, otherwise {@code false}.
     * @return A created named binary tag writer.
     */
    NBTWriter create(@Assisted("output") File file, @Assisted("compressed") boolean compressed);
  }
}
