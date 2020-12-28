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

package net.flintmc.mcapi.internal.nbt.io.read;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.NBT;
import net.flintmc.mcapi.nbt.io.read.NBTDataInputStream;
import net.flintmc.mcapi.nbt.io.read.NBTReader;
import net.flintmc.util.commons.Pair;

import java.io.*;
import java.util.zip.GZIPInputStream;

/** Default implementation of the {@link NBTReader}. */
@Implement(NBTReader.class)
public class DefaultNBTReader implements NBTReader {

  private final NBTDataInputStream inputStream;

  @AssistedInject
  private DefaultNBTReader(
      NBTDataInputStream.Factory factory,
      @Assisted("input") InputStream inputStream,
      @Assisted("compressed") boolean compressed)
      throws IOException {
    this.inputStream =
        factory.create(
            new DataInputStream(compressed ? new GZIPInputStream(inputStream) : inputStream));
  }

  @AssistedInject
  private DefaultNBTReader(
      NBTDataInputStream.Factory provider,
      @Assisted("file") File file,
      @Assisted("compressed") boolean compressed)
      throws IOException {
    this(provider, new BufferedInputStream(new FileInputStream(file)), compressed);
  }

  /** {@inheritDoc} */
  @Override
  public Pair<String, NBT> readNamed() throws IOException {
    return this.inputStream.readFullyFormedTag();
  }

  /** {@inheritDoc} */
  @Override
  public NBT read() throws IOException {
    return this.readNamed().getSecond();
  }

  /** {@inheritDoc} */
  @Override
  public NBT readRaw(int identifier) throws IOException {
    return this.inputStream.readTag(identifier);
  }

  /** {@inheritDoc} */
  @Override
  public void close() {
    try {
      this.inputStream.getDataInputStream().close();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }
}
