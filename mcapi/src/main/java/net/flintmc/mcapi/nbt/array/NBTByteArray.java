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

package net.flintmc.mcapi.nbt.array;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.nbt.NBT;

/**
 * An array of bytes.
 */
public interface NBTByteArray extends NBT {

  /**
   * Retrieves the array of the named binary tag.
   *
   * @return The array of the named binary tag.
   */
  byte[] asArray();

  /**
   * A factory for {@link NBTByteArray}.
   */
  @AssistedFactory(NBTByteArray.class)
  interface Factory {

    /**
     * Creates a new {@link NBTByteArray} with the given array.
     *
     * @param value The array for the named binary tag.
     * @return A created byt array named binary tag.
     */
    NBTByteArray create(@Assisted("value") byte[] value);
  }
}
