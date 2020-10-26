package net.labyfy.internal.component.tileentity.cache;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.tileentity.TileEntity;
import net.labyfy.component.world.math.BlockPosition;

import java.util.Map;
import java.util.function.Supplier;

/**
 * This cache is used to store all tile entities ported from Minecraft to Labyfy to save resources.
 * <p>
 * This cache is cleared when the player leaves the world or server.
 * <p>
 * To cache a tile entity {@link #putIfAbsent(BlockPosition, Supplier)} is used, this will cache the
 * given tile entity and its block position.
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
   * If the given block position is already associated with a tile entity, the associated ile entity is returned.
   * If the specified block position is not associated with a tile entity associates the block position with
   * the given supplied tile entity.
   *
   * @param blockPosition The block position with the specified tile entity is to be associated.
   * @param supplier The tile entity to be associated with the specified block position.
   * @return The previous tile entity associated with the specified block position, or a the given supplied
   * tile entity if there was not mapping for the block position.
   */
  public TileEntity putIfAbsent(BlockPosition blockPosition, Supplier<TileEntity> supplier) {
    if (this.tileEntities.containsKey(blockPosition)) {
      return this.getTileEntity(blockPosition);
    }
    return this.tileEntities.put(blockPosition, supplier.get());
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
