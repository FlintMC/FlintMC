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
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.math.BlockPosition;

/**
 * Represents a state of a block. Every block type consists of multiple block states. If a block is
 * changed (for example redstone wire in a specific direction powered or not powered) it also has a
 * different block state.
 *
 * @see BlockType
 * @see BlockType#getDefaultState()
 * @see World#getBlockState(int, int, int)
 * @see World#getBlockState(BlockPosition)
 */
public interface BlockState {

  /**
   * Retrieves the version-specific handle of this block state. Only intended to be used
   * internally.
   *
   * @param <T> The Minecraft type
   * @return The non-null handle
   */
  <T> T getHandle();

  /**
   * Retrieves the block type that is represented by this state.
   *
   * @return The non-null block type of this state
   */
  BlockType getType();

  /**
   * Retrieves whether or not the randomTickSpeed gamerule applies to this block state.
   *
   * @return {@code true} if it applies to this block state, {@code false} otherwise
   */
  boolean ticksRandomly();

  /**
   * Retrieves whether or not this block is solid. For example stone is solid, fences are not.
   *
   * @return {@code true} if it is solid, {@code false} otherwise
   */
  boolean isSolid();

  /**
   * Retrieves whether or not this block is solid. For example stone is not transparent, end portal
   * frames are.
   *
   * @return {@code true} if it is transparent, {@code false} otherwise
   */
  boolean isTransparent();

  /**
   * Retrieves the light value of this block state.
   *
   * @return The light value as an integer in the range from 0 to 15
   */
  int getLightValue();

  /**
   * Retrieves whether or not this block state overrides the state of a comparator which is for
   * example chests, hoppers, etc.
   *
   * @return {@code true} if this block state has a comparator override, {@code false} otherwise
   */
  boolean hasComparatorInputOverride();

  /**
   * Retrieves whether or not this block state can provide redstone power output. This only states
   * whether it CAN provide redstone power, so for example a lever that is turned off can also
   * provide redstone power.
   *
   * @return {@code true} if it can provide
   */
  boolean canProvidePower();

  /**
   * Factory for the {@link BlockState}. Consider using {@link World#getBlockState(int, int, int)}
   * or {@link BlockType#getDefaultState()} instead as this should only be used internally.
   */
  @AssistedFactory(BlockState.class)
  interface Factory {

    /**
     * Creates a new {@link BlockState} for the given Minecraft BlockState object and gets the
     * {@link BlockType} from the given object.
     *
     * @param handle The non-null Minecraft BlockState object
     * @return The new non-null {@link BlockState}
     */
    BlockState create(@Assisted("handle") Object handle);

    /**
     * Creates a new {@link BlockState} for the given Minecraft BlockState object and type.
     *
     * @param type   The non-null type of the block, needs to match the one in the given Minecraft
     *               BlockState object
     * @param handle The non-null Minecraft BlockState object
     * @return The new non-null {@link BlockState}
     */
    BlockState create(@Assisted BlockType type, @Assisted("handle") Object handle);

  }

}
