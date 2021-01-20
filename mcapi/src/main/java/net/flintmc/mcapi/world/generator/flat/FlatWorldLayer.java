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

package net.flintmc.mcapi.world.generator.flat;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.world.block.BlockState;
import net.flintmc.mcapi.world.block.BlockType;

/**
 * Represents a layer of one block type in a {@link FlatWorldGeneratorSettings}.
 */
public interface FlatWorldLayer {

  /**
   * Retrieves the block state that should be generated for this layer.
   *
   * @return The non-null block state of this layer
   */
  BlockState getBlockState();

  /**
   * Retrieves the height of this layer in blocks.
   *
   * @return The height of this layer in blocks, always &gt; 0
   */
  int getLayerHeight();

  /**
   * Factory for the {@link FlatWorldLayer}.
   */
  @AssistedFactory(FlatWorldLayer.class)
  interface Factory {

    /**
     * Creates a new {@link FlatWorldLayer}.
     *
     * @param blockState  The non-null block state of this layer
     * @param layerHeight The height of this layer in blocks, needs to be &gt; 0
     * @return The new non-null {@link FlatWorldLayer}
     */
    FlatWorldLayer create(
        @Assisted BlockState blockState, @Assisted("layerHeight") int layerHeight);

    /**
     * Creates a new {@link FlatWorldLayer} with the {@link BlockType#getDefaultState() default
     * block state} of the given block type.
     *
     * @param blockType   The non-null block type of this layer to get the default state from
     * @param layerHeight The height of this layer in blocks, needs to be &gt; 0
     * @return The new non-null {@link FlatWorldLayer}
     */
    FlatWorldLayer create(
        @Assisted BlockType blockType, @Assisted("layerHeight") int layerHeight);

  }

}
