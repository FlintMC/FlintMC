package net.flintmc.mcapi.player;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents the remote client player.
 */
public interface RemoteClientPlayer extends PlayerEntity, BaseClientPlayer {

  /**
   * {@inheritDoc}
   */
  @Override
  default boolean isSpectator() {
    return false;
  }

  /**
   * A factory class for the {@link RemoteClientPlayer}.
   */
  @AssistedFactory(RemoteClientPlayer.class)
  interface Factory {

    /**
     * Creates a new {@link RemoteClientPlayer} with the given parameters.
     *
     * @param entity     The non-null Minecraft entity.
     * @param entityType The entity type.
     * @return A created {@link RemoteClientPlayer}.
     */
    RemoteClientPlayer create(
            @Assisted("entity") Object entity,
            @Assisted("entityType") EntityType entityType
    );

  }

  /**
   * Service interface for creating {@link RemoteClientPlayer}'s.
   */
  interface Provider {

    /**
     * Creates a new {@link RemoteClientPlayer} with the given parameter.
     *
     * @param entity The non-null Minecraft entity.
     * @return A created {@link RemoteClientPlayer}.
     */
    RemoteClientPlayer get(Object entity);

  }

}
