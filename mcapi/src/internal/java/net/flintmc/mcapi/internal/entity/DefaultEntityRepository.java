package net.flintmc.mcapi.internal.entity;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.EntityRepository;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * This cache is used to store all entities ported from Minecraft to Flint to save resources.
 *
 * <p>This cache is cleared when the player leaves the world or server.
 *
 * <p>To cache an entity {@link #putIfAbsent(UUID, Supplier)} is used, this will cache the given
 * entity and its unique identifier.
 */
@Singleton
@Implement(EntityRepository.class)
public class DefaultEntityRepository implements EntityRepository {

  private final Map<UUID, Entity> entities;

  @Inject
  private DefaultEntityRepository() {
    this.entities = Maps.newHashMap();
  }

  /**
   * Retrieves an entity with the given unique identifier.
   *
   * @param uniqueId The unique identifier of a cached entity.
   * @return A cached entity or {@code null}.
   */
  @Override
  public Entity getEntity(UUID uniqueId) {
    return this.entities.get(uniqueId);
  }

  /**
   * If the given unique identifier is already associated with an entity, the associated entity is
   * returned. If the specified unique identifier is not associated with an entity associates the
   * unique identifier with the given supplied entity.
   *
   * @param uniqueId The unique identifier with the specified entity is to be associated.
   * @param supplier The entity to be associated with the specified unique identifier.
   * @return The previous entity associated with the specified unique identifier, or a the given
   *     supplied entity if there was not mapping for the unique identifier.
   */
  @Override
  public Entity putIfAbsent(UUID uniqueId, Supplier<Entity> supplier) {
    if (this.entities.containsKey(uniqueId)) {
      return this.getEntity(uniqueId);
    }
    Entity suppliedEntity = supplier.get();
    this.entities.put(uniqueId, suppliedEntity);
    return suppliedEntity;
  }

  /** Clears the cache. */
  @Override
  public void clear() {
    this.entities.clear();
  }

  /**
   * Retrieves the size of the cache.
   *
   * @return The cache size.
   */
  @Override
  public int size() {
    return this.entities.size();
  }

  @Override
  public Map<UUID, Entity> getEntities() {
    return entities;
  }
}
