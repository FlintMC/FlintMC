package net.flintmc.mcapi.entity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.world.math.BlockPosition;

/**
 * Represents the Minecraft creature entity.
 */
public interface CreatureEntity extends MobEntity {

  /**
   * Retrieves the block path weight of the creature entity.
   *
   * @param position The block position of the entity.
   * @return The block path weight of the creature entity.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getBlockPathWeight(BlockPosition position);

  /**
   * Whether the creature entity has a path.
   *
   * @return {@code true} if the creature entity has a path, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean hasPath();

  /**
   * A factory class for the {@link CreatureEntity}.
   */
  @AssistedFactory(CreatureEntity.class)
  interface Factory {

    /**
     * Creates a new {@link CreatureEntity} with the given parameters.
     *
     * @param entity     The entity.
     * @param entityType The type of the entity.
     * @return A created creature entity.
     */
    CreatureEntity create(
        @Assisted("entity") Object entity, @Assisted("entityType") EntityType entityType);
  }

  /**
   * Service interface for creating {@link CreatureEntity}'s.
   */
  interface Provider {

    /**
     * Creates a new {@link CreatureEntity}.
     *
     * @param entity The entity.
     * @return A created creature entity.
     */
    CreatureEntity get(Object entity);
  }
}
