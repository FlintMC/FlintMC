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

package net.flintmc.mcapi.internal.nbt;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.NBTShort;
import net.flintmc.mcapi.nbt.NBTType;
import net.flintmc.mcapi.nbt.io.read.NBTDataInputStream;
import net.flintmc.mcapi.nbt.io.write.NBTDataOutputStream;

import java.io.IOException;

/** Default implementation the {@link NBTShort}. */
@Implement(NBTShort.class)
public class DefaultNBTShort implements NBTShort {

  private short value;

  @AssistedInject
  private DefaultNBTShort(@Assisted("value") short value) {
    this.value = value;
  }

  @Override
  public NBTType getIdentifier() {
    return NBTType.TAG_INT;
  }

  @Override
  public void readContents(NBTDataInputStream inputStream) throws IOException {
    this.value = inputStream.getDataInputStream().readShort();
  }

  @Override
  public void writeContents(NBTDataOutputStream outputStream) throws IOException {
    outputStream.getDataOutputStream().writeShort(this.value);
  }

  /** {@inheritDoc} */
  @Override
  public String asString() {
    return String.valueOf(this.value);
  }

  /** {@inheritDoc} */
  @Override
  public short asShort() {
    return this.value;
  }
}
