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
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.resources.ResourceLocation;

/**
 * Represents a type of a block, for example minecraft:stone.
 */
public interface BlockType {

  /**
   * Retrieves the version-specific handle of this block type. Only intended to be used internally.
   *
   * @param <T> The Minecraft type
   * @return The non-null handle
   */
  <T> T getHandle();

  /**
   * Retrieves the unique name of this block type, for example minecraft:stone.
   *
   * @return The non-null unique name of this block type
   */
  ResourceLocation getName();

  /**
   * Retrieves the default {@link BlockState} of this block type. Every block type consists of
   * multiple block states. If a block is changed (for example redstone wire in a specific direction
   * powered or not powered) it also has a different block state.
   *
   * @return The non-null default block state of this block type
   */
  BlockState getDefaultState();

  /**
   * Retrieves whether this block type has a tile entity. A tile entity is an entity which is
   * exactly at the position of a block but can store more information about it.
   *
   * @return {@code true} if this block type can create tile entities, {@code false} otherwise
   */
  boolean hasTileEntity();

  /**
   * Retrieves the explosion resistance of this block type.
   *
   * @return The explosion resistance of this block type
   */
  float getExplosionResistance();

  /**
   * Retrieves the display name of this item which is generally a translation component.
   *
   * @return The non-null display name component of this block type
   */
  ChatComponent getDisplayName();

  /**
   * Retrieves the slipperiness of this block type.
   *
   * @return The slipperiness of this block type
   */
  float getSlipperiness();

  /**
   * Retrieves the jump factory of this block type.
   *
   * @return The jump factory of this block type
   */
  float getSpeedFactor();

  /**
   * Retrieves the jump factory of this block type.
   *
   * @return The jump factory of this block type
   */
  float getJumpFactor();

  /**
   * Factory for the {@link BlockType}. Consider using the {@link BlockTypeRegistry} instead as this
   * should only be used internally.
   */
  @AssistedFactory(BlockType.class)
  interface Factory {

    /**
     * Creates a new {@link BlockType} for the given parameters.
     *
     * @param name   The non-null and unique name of the new block type
     * @param handle The non-null Minecraft instance of the new block type
     * @return The new non-null {@link BlockType}
     */
    BlockType create(@Assisted ResourceLocation name, @Assisted("handle") Object handle);

  }

}
