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

package net.flintmc.mcapi.world.block;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.type.ItemType;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.math.BlockPosition;

/**
 * Represents a {@link BlockState} at a specific position placed in a world.
 *
 * @see BlockState
 * @see BlockType
 * @see World#getBlock(int, int, int)
 * @see World#getBlock(BlockPosition)
 */
public interface Block {

  /**
   * Retrieves the version-specific handle of this block. Only intended to be used internally.
   *
   * @param <T> The Minecraft type
   * @return The non-null handle
   */
  <T> T getHandle();

  /**
   * Retrieves the position where this block is located at in the {@link World}.
   *
   * @return The non-null position of this block
   */
  BlockPosition getPosition();

  /**
   * Retrieves the block state of this block.
   *
   * @return The non-null block state of this block
   */
  BlockState getState();

  /**
   * Retrieves the hardness of this block.
   *
   * @return The hardness of the block in the range from 0 to 5 or {@code -1} if this block is
   * unbreakable without being in creative mode
   */
  float getBlockHardness();

  /**
   * Retrieves the redstone power that is emitted by a comparator next to this block.
   *
   * @return The redstone power for comparators in the range from 0 to 15 or {@code 0} if this block
   * doesn't emit redstone from comparators next to it
   */
  int getComparatorInputOverride();

  /**
   * Creates a new item stack with the type of the item of this block. Equivalent to {@link
   * ItemType#createStack()} with {@link #getItemType()}.
   *
   * @return The new non-null item stack
   * @see #getItemType()
   */
  ItemStack createItemStack();

  /**
   * Retrieves the item type as it could be added to an inventory. For example for stone as the
   * block the item will also be stone and not cobblestone.
   *
   * @return The non-null item type of this block
   */
  ItemType getItemType();

  /**
   * Factory for the {@link Block}.
   */
  @AssistedFactory(Block.class)
  interface Factory {

    /**
     * Creates a new {@link Block} and gets the data from the block at the given position in the
     * world. Equivalent to {@link World#getBlock(BlockPosition)}.
     *
     * @param position The non-null position to get the data about the block from
     * @return The new non-null {@link Block} with the data from the world at the given position
     */
    Block create(@Assisted BlockPosition position);

  }

}
