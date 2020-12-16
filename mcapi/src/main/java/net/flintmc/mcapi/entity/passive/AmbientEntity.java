package net.flintmc.mcapi.entity.passive;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.MobEntity;
import net.flintmc.mcapi.entity.type.EntityType;

/**
 * Represents a Minecraft ambient entity.
 */
public interface AmbientEntity extends MobEntity {

  /**
   * A factory class for the {@link AmbientEntity}.
   */
  @AssistedFactory(AmbientEntity.class)
  interface Factory {

    /**
     * Creates a new {@link AmbientEntity} with the given parameters.
     *
     * @param entity     The entity.
     * @param entityType The type of the entity.
     * @return A created ambient entity.
     */
    AmbientEntity create(
        @Assisted("entity") Object entity, @Assisted("entityType") EntityType entityType);
  }

  /**
   * Service interface for creating {@link AmbientEntity}'s.
   */
  interface Provider {

    /**
     * Creates a new {@link AmbientEntity} with the given entity.
     *
     * @param entity The entity.
     * @return A created ambient entity.
     * @see AmbientEntity.Factory#create(Object, EntityType)
     */
    AmbientEntity get(Object entity);
  }
}
