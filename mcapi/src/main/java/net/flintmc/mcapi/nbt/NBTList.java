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
 * A list of tag payloads, without repeated tag ID's or any tag names.
 */
public interface NBTList extends NBT, Iterable<NBT> {

  /**
   * Appends a tag to the list.
   *
   * @param tag The new tag.
   */
  void add(NBT tag);

  /**
   * Changes the tag at the given index.
   *
   * @param index The index for the tag.
   * @param tag   The new tag.
   */
  void set(int index, NBT tag);

  /**
   * Retrieves the tag at the given index.
   *
   * @param index The index of a tag.
   * @return The tag at the given index.
   */
  NBT get(int index);

  /**
   * Removes all tags from this list.
   */
  void clear();

  /**
   * Retrieves the size of the collection.
   */
  int size();

  /**
   * A factory class for the {@link NBTList}.
   */
  @AssistedFactory(NBTList.class)
  interface Factory {

    /**
     * Creates a new {@link NBTList} with the given subtag identifier.
     *
     * @param subtagIdentifier The subtag identifier to create the list
     * @return A created list named binary tag.
     */
    NBTList create(@Assisted("subtagIdentifier") int subtagIdentifier);
  }
}
