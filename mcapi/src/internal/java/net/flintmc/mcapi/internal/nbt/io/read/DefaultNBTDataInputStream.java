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
import net.flintmc.mcapi.nbt.NBTCreator;
import net.flintmc.mcapi.nbt.NBTList;
import net.flintmc.mcapi.nbt.NBTType;
import net.flintmc.mcapi.nbt.io.read.NBTDataInputStream;
import net.flintmc.util.commons.Pair;

import java.io.DataInputStream;
import java.io.IOException;

/** Default implementation of the {@link NBTDataInputStream}. */
@Implement(NBTDataInputStream.class)
public class DefaultNBTDataInputStream implements NBTDataInputStream {

  private final DataInputStream dataInputStream;
  private final NBTCreator nbtCreator;

  @AssistedInject
  private DefaultNBTDataInputStream(
      @Assisted("inputStream") DataInputStream dataInputStream, NBTCreator nbtCreator) {
    this.dataInputStream = dataInputStream;
    this.nbtCreator = nbtCreator;
  }

  /** {@inheritDoc} */
  @Override
  public Pair<String, NBT> readFullyFormedTag() throws IOException {
    int identifier = this.dataInputStream.readByte();

    if (identifier == NBTType.TAG_END.getIdentifier()) {
      return new Pair<>("", this.nbtCreator.createNbtEnd());
    }
    String name = this.dataInputStream.readUTF();

    return new Pair<>(name, this.readTag(identifier));
  }

  /** {@inheritDoc} */
  @Override
  public NBT readTag(int identifier) throws IOException {
    NBTType type = NBTType.getNbt(identifier);

    if (type == null) {
      throw new NullPointerException("");
    }

    switch (type) {
      case TAG_END:
        return this.nbtCreator.createNbtEnd();
      case TAG_BYTE:
        return this.nbtCreator.createNbtByte((byte) 0);
      case TAG_SHORT:
        return this.nbtCreator.createNbtShort((short) 0);
      case TAG_INT:
        return this.nbtCreator.createNbtInt(0);
      case TAG_LONG:
        return this.nbtCreator.createNbtLong(0L);
      case TAG_FLOAT:
        return this.nbtCreator.createNbtFloat(0.0F);
      case TAG_DOUBLE:
        return this.nbtCreator.createNbtDouble(0.0D);
      case TAG_BYTE_ARRAY:
        return this.nbtCreator.createNbtByteArray(new byte[0]);
      case TAG_STRING:
        return this.nbtCreator.createNbtString("");
      case TAG_LIST:
        int subtagType = this.dataInputStream.readByte();
        NBTList list = this.nbtCreator.createNbtList(subtagType);
        list.readContents(this);
        return list;
      case TAG_COMPOUND:
        return this.nbtCreator.createNbtCompound();
      case TAG_INT_ARRAY:
        return this.nbtCreator.createNbtIntArray(new int[0]);
      case TAG_LONG_ARRAY:
        return this.nbtCreator.createNbtLongArray(new long[0]);
      default:
        throw new IllegalStateException("Unexpected value: " + type);
    }
  }

  /** {@inheritDoc} */
  @Override
  public DataInputStream getDataInputStream() {
    return this.dataInputStream;
  }
}
