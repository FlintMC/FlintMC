package net.labyfy.internal.component.entity.cache;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.processing.autoload.AutoLoad;

import java.util.Map;
import java.util.UUID;

/**
 * This cache is used to store all entities ported from Minecraft to Labyfy to save resources.
 * <p>
 * This cache is cleared when the player leaves the world or server.
 * <p>
 * To cache an entity {@link #putAndRetrieveEntity(UUID, Entity)} is used, this will cache the given entity and
 * its unique identifier.
 */
@Singleton
@AutoLoad
public class EntityCache {

  private final Map<UUID, Entity> entities;

  @Inject
  private EntityCache() {
    this.entities = Maps.newHashMap();
  }

  /**
   * Puts and retrieves the cached entity.
   *
   * @param uniqueId The unique identifier of this entity.
   * @param entity   The Labyfy entity to be cached.
   * @return The cached Labyfy entity.
   */
  public Entity putAndRetrieveEntity(UUID uniqueId, Entity entity) {
    return this.entities.putIfAbsent(uniqueId, entity);
  }

  /**
   * Retrieves an entity with the given unique identifier.
   *
   * @param uniqueId The unique identifier of a cached entity.
   * @return A cached entity or {@code null}.
   */
  public Entity getEntity(UUID uniqueId) {
    return this.entities.get(uniqueId);
  }

  /**
   * Whether the given unique identifier is already cached.
   *
   * @param uniqueId The unique identifier of a cached entity.
   * @return {@code true} if the given unique identifier is already cached, otherwise {@code false}.
   */
  public boolean isCached(UUID uniqueId) {
    return this.entities.containsKey(uniqueId);
  }

  /**
   * Clears the cache.
   */
  public void clear() {
    this.entities.clear();
  }

  /**
   * Retrieves the size of the cache.
   *
   * @return The cache size.
   */
  public int size() {
    return this.entities.size();
  }
}
