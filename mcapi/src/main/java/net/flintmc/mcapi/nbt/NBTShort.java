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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * A singed integral type.
 */
public interface NBTShort extends NBT {

  /**
   * Retrieves the short of the named binary tag.
   *
   * @return The short of the named binary tag.
   */
  short asShort();

  /**
   * A factory class for the {@link NBTShort}.
   */
  @AssistedFactory(NBTShort.class)
  interface Factory {

    /**
     * Creates a new {@link NBTShort} with the given value.
     *
     * @param value The value for the named binary tag.
     * @return A created short named binary tag.
     */
    NBTShort create(@Assisted("value") short value);
  }
}
