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

import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Used to mark the end of compound tags. This tag does not have a name, so it is only ever a single
 * byte {@code 0}. It may also be the type of empty List tags.
 */
public interface NBTEnd extends NBT {

  /**
   * A factory class for the {@link NBTEnd}.
   */
  @AssistedFactory(NBTEnd.class)
  interface Factory {

    /**
     * Creates a new {@link NBTEnd}.
     *
     * @return A created end named binary tag.
     */
    NBTEnd create();
  }
}
