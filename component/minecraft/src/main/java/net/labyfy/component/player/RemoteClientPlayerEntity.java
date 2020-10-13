package net.labyfy.component.player;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.assisted.AssistedFactory;

/**
 * Represents the remote client player.
 */
public interface RemoteClientPlayerEntity extends AbstractClientPlayerEntity {

  /**
   * A factory class for the {@link RemoteClientPlayerEntity}.
   */
  @AssistedFactory(RemoteClientPlayerEntity.class)
  interface Factory {

    /**
     * Creates a new {@link RemoteClientPlayerEntity} with the given parameters.
     *
     * @param entity     The non-null Minecraft entity.
     * @param entityType The entity type.
     * @return A created {@link RemoteClientPlayerEntity}.
     */
    RemoteClientPlayerEntity create(
            @Assisted("entity") Object entity,
            @Assisted("entityType") EntityType entityType
    );

  }

  /**
   * Service interface for creating {@link RemoteClientPlayerEntity}'s.
   */
  interface Provider {

    /**
     * Creates a new {@link RemoteClientPlayerEntity} with the given parameter.
     *
     * @param entity The non-null Minecraft entity.
     * @return A created {@link RemoteClientPlayerEntity}.
     */
    RemoteClientPlayerEntity get(Object entity);

  }

}
