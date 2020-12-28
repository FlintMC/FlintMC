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
import net.flintmc.mcapi.nbt.NBTLong;
import net.flintmc.mcapi.nbt.NBTType;
import net.flintmc.mcapi.nbt.io.read.NBTDataInputStream;
import net.flintmc.mcapi.nbt.io.write.NBTDataOutputStream;

import java.io.IOException;

/** Default implementation the {@link NBTLong}. */
@Implement(NBTLong.class)
public class DefaultNBTLong implements NBTLong {

  private long value;

  @AssistedInject
  private DefaultNBTLong(@Assisted("value") long value) {
    this.value = value;
  }

  @Override
  public NBTType getIdentifier() {
    return NBTType.TAG_LONG;
  }

  /** {@inheritDoc} */
  @Override
  public void readContents(NBTDataInputStream inputStream) throws IOException {
    this.value = inputStream.getDataInputStream().readLong();
  }

  /** {@inheritDoc} */
  @Override
  public void writeContents(NBTDataOutputStream outputStream) throws IOException {
    outputStream.getDataOutputStream().writeLong(this.value);
  }

  /** {@inheritDoc} */
  @Override
  public String asString() {
    return String.valueOf(this.value);
  }

  /** {@inheritDoc} */
  @Override
  public long asLong() {
    return this.value;
  }
}
