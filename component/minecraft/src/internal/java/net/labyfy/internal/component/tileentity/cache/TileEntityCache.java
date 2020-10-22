package net.labyfy.internal.component.tileentity.cache;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.tileentity.TileEntity;
import net.labyfy.component.world.math.BlockPosition;

import java.util.Map;

/**
 * This cache is used to store all tile entities ported from Minecraft to Labyfy to save resources.
 * <p>
 * This cache is cleared when the player leaves the world or server.
 * <p>
 * To cache a tile entity {@link #putAndRetrieveTileEntity(BlockPosition, TileEntity)} is used, this will cache the
 * given tile entity and its block position.
 */
@Singleton
@AutoLoad
public class TileEntityCache {

  private final Map<BlockPosition, TileEntity> tileEntities;

  @Inject
  private TileEntityCache() {
    this.tileEntities = Maps.newHashMap();
  }

  /**
   * Puts and retrieves the cached tile entity.
   *
   * @param blockPosition The block position of this tile entity.
   * @param entity        The Labyfy tile entity to be cached.
   * @return The cached Labyfy tile entity.
   */
  public TileEntity putAndRetrieveTileEntity(BlockPosition blockPosition, TileEntity entity) {
    return this.tileEntities.putIfAbsent(blockPosition, entity);
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
   * Whether the given block position is already cached.
   *
   * @param blockPosition The block position of a cached tile entity.
   * @return {@code true} if the given block position is already cached, otherwise {@code false}.
   */
  public boolean isCached(BlockPosition blockPosition) {
    return this.tileEntities.containsKey(blockPosition);
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
