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

import net.flintmc.mcapi.nbt.array.NBTByteArray;
import net.flintmc.mcapi.nbt.array.NBTIntArray;
import net.flintmc.mcapi.nbt.array.NBTLongArray;

/**
 * An interface for creating various named binary tags.
 */
public interface NBTCreator {

  /**
   * Creates a new {@link NBTByte}.
   *
   * @param value The value for the byte named binary tag.
   * @return A created byte named binary tag.
   */
  NBTByte createNbtByte(byte value);

  /**
   * Creates a new {@link NBTCompound}.
   *
   * @return A created compound named binary tag.
   */
  NBTCompound createNbtCompound();

  /**
   * Creates a new {@link NBTDouble}.
   *
   * @param value The value for the double named binary tag.
   * @return A created double named binary tag.
   */
  NBTDouble createNbtDouble(double value);

  /**
   * Creates a new {@link NBTEnd}.
   *
   * @return A created end named binary tag.
   */
  NBTEnd createNbtEnd();

  /**
   * Creates a new {@link NBTFloat}.
   *
   * @param value The value for the float named binary tag.
   * @return A created float named binary tag.
   */
  NBTFloat createNbtFloat(float value);

  /**
   * Creates a new {@link NBTInt}.
   *
   * @param value The value for the int named binary tag.
   * @return A created int named binary tag.
   */
  NBTInt createNbtInt(int value);

  /**
   * Creates a new {@link NBTList}.
   *
   * @param subtagIdentifier The subtag identifier for the list named binary tag.
   * @return A created list named binary tag.
   */
  NBTList createNbtList(int subtagIdentifier);

  /**
   * Creates a new {@link NBTLong}.
   *
   * @param value The value for the long named binary tag.
   * @return A created long named binary tag.
   */
  NBTLong createNbtLong(long value);

  /**
   * Creates a new {@link NBTShort}.
   *
   * @param value The value for the short named binary tag.
   * @return A created short named binary tag.
   */
  NBTShort createNbtShort(short value);

  /**
   * Creates a new {@link NBTString}.
   *
   * @param value The value for the string named binary tag.
   * @return A created string named binary tag.
   */
  NBTString createNbtString(String value);

  /**
   * Creates a new {@link NBTByteArray}.
   *
   * @param value The value for the byte array named binary tag.
   * @return A created byte array named binary tag.
   */
  NBTByteArray createNbtByteArray(byte[] value);

  /**
   * Creates a new {@link NBTIntArray}.
   *
   * @param value The value for the integer array named binary tag.
   * @return A created integer array named binary tag.
   */
  NBTIntArray createNbtIntArray(int[] value);

  /**
   * Creates a new {@link NBTLongArray}.
   *
   * @param value The value for the long array named binary tag.
   * @return A created long array named binary tag.
   */
  NBTLongArray createNbtLongArray(long[] value);
}
