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

package net.flintmc.mcapi.internal.tileentity.cache;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map;
import java.util.function.Supplier;
import net.flintmc.mcapi.tileentity.TileEntity;
import net.flintmc.mcapi.world.math.BlockPosition;

/**
 * This cache is used to store all tile entities ported from Minecraft to Flint to save resources.
 *
 * <p>This cache is cleared when the player leaves the world or server.
 *
 * <p>To cache a tile entity {@link #putIfAbsent(BlockPosition, Supplier)} is used, this will cache
 * the given tile entity and its block position.
 */
@Singleton
public class TileEntityCache {

  private final Map<BlockPosition, TileEntity> tileEntities;

  @Inject
  private TileEntityCache() {
    this.tileEntities = Maps.newHashMap();
  }

  /**
   * Retrieves an tile entity with the given block position.
   *
   * @param blockPosition The block position of a cached tile entity.
   * @return A cached entity or {@code null}.
   */
  public TileEntity getTileEntity(BlockPosition blockPosition) {
    return this.tileEntities.get(blockPosition);
  }

  /**
   * If the given block position is already associated with a tile entity, the associated ile entity
   * is returned. If the specified block position is not associated with a tile entity associates
   * the block position with the given supplied tile entity.
   *
   * @param blockPosition The block position with the specified tile entity is to be associated.
   * @param supplier      The tile entity to be associated with the specified block position.
   * @return The previous tile entity associated with the specified block position, or a the given
   * supplied tile entity if there was not mapping for the block position.
   */
  public TileEntity putIfAbsent(BlockPosition blockPosition, Supplier<TileEntity> supplier) {
    if (this.tileEntities.containsKey(blockPosition)) {
      return this.getTileEntity(blockPosition);
    }
    TileEntity suppliedEntity = supplier.get();
    this.tileEntities.put(blockPosition, suppliedEntity);
    return suppliedEntity;
  }

  /**
   * Clears the cache.
   */
  public void clear() {
    this.tileEntities.clear();
  }

  /**
   * Retrieves the size of the cache.
   *
   * @return The cache size.
   */
  public int size() {
    return this.tileEntities.size();
  }
}
