package net.flintmc.mcapi.entity.ai;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.MobEntity;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents the Minecraft entity senses.
 */
public interface EntitySenses {

  /**
   * Clears every tick all lists.
   */
  void tick();

  /**
   * Whether the entity senses can see an entity.
   *
   * @param entity The entity.
   * @return {@code true} if the entity senses can see an entity.
   */
  boolean canSeeEntity(Entity entity);

  /**
   * A factory class for the {@link EntitySenses}.
   */
  @AssistedFactory(EntitySenses.class)
  interface Factory {

    /**
     * Creates a new {@link EntitySenses} from the given {@link MobEntity}.
     *
     * @param entity The mob entity.
     * @return A created entity senses.
     */
    EntitySenses create(@Assisted("mobEntity") MobEntity entity);

  }

}
