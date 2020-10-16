package net.labyfy.component.entity.projectile;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.assisted.AssistedFactory;

/**
 * Represents the Minecraft throwable entity.
 */
public interface ThrowableEntity extends Entity, Projectile {

  /**
   * Shoots the throwable entity.
   *
   * @param thrower     The thrower of the throwable entity.
   * @param pitch       The pitch for the throwable entity.
   * @param yaw         The yaw for the throwable entity.
   * @param pitchOffset The pitch offset for the throwable entity.
   * @param velocity    The velocity for the throwable entity.
   * @param inaccuracy  The inaccuracy for the throwable entity.
   * @see Projectile#shoot(double, double, double, float, float)
   */
  void shoot(Entity thrower, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy);

  /**
   * Retrieves the thrower of the entity.
   *
   * @return The entity thrower.
   */
  LivingEntity getThrower();

  /**
   * A factory class for the {@link ThrowableEntity}.
   */
  @AssistedFactory(ThrowableEntity.class)
  interface Factory {

    /**
     * Creates a new {@link ThrowableEntity} with the given parameters.
     *
     * @param entity     The throwable entity.
     * @param entityType The type of the throwable entity.
     * @return A created throwable entity.
     */
    ThrowableEntity create(@Assisted("entity") Object entity, @Assisted("entityType") EntityType entityType);

  }

  /**
   * Service interface for creating {@link ThrowableEntity}'s.
   */
  interface Provider {

    /**
     * Creates a new {@link ThrowableEntity} with the given entity.
     *
     * @param entity     The throwable entity.
     * @return A created throwable entity.
     */
    ThrowableEntity get(Object entity);

  }

}

