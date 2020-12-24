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

package net.flintmc.mcapi.internal.nbt.io.write;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.NBT;
import net.flintmc.mcapi.nbt.io.write.NBTDataOutputStream;
import net.flintmc.mcapi.nbt.io.write.NBTWriter;

import java.io.*;
import java.util.zip.GZIPOutputStream;

/** Default implementation of the {@link NBTWriter}. */
@Implement(NBTWriter.class)
public class DefaultNBTWriter implements NBTWriter {

  private final NBTDataOutputStream outputStream;

  @AssistedInject
  private DefaultNBTWriter(
      NBTDataOutputStream.Factory factory,
      @Assisted("output") OutputStream outputStream,
      @Assisted("compressed") boolean compressed)
      throws IOException {
    this.outputStream =
        factory.create(
            new DataOutputStream(compressed ? new GZIPOutputStream(outputStream) : outputStream));
  }

  @AssistedInject
  private DefaultNBTWriter(
      NBTDataOutputStream.Factory factory,
      @Assisted("output") File file,
      @Assisted("compressed") boolean compressed)
      throws IOException {
    this(factory, new BufferedOutputStream(new FileOutputStream(file)), compressed);
  }

  /** {@inheritDoc} */
  @Override
  public void writeNamed(String name, NBT tag) throws IOException {
    this.outputStream.writeFullyFormedTag(name, tag);
  }

  /** {@inheritDoc} */
  @Override
  public void writeRaw(NBT tag) throws IOException {
    tag.writeContents(this.outputStream);
  }

  /** {@inheritDoc} */
  @Override
  public void close() throws IOException {
    this.outputStream.getDataOutputStream().close();
  }
}
