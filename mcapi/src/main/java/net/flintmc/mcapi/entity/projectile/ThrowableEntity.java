package net.flintmc.mcapi.entity.projectile;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.projectile.type.Projectile;
import net.flintmc.mcapi.entity.type.EntityType;

/** Represents the Minecraft throwable entity. */
public interface ThrowableEntity extends Entity, Projectile {

  /**
   * Shoots the throwable entity.
   *
   * @param thrower The thrower of the throwable entity.
   * @param pitch The pitch for the throwable entity.
   * @param yaw The yaw for the throwable entity.
   * @param pitchOffset The pitch offset for the throwable entity.
   * @param velocity The velocity for the throwable entity.
   * @param inaccuracy The inaccuracy for the throwable entity.
   * @see Projectile#shoot(double, double, double, float, float)
   */
  void shoot(
      Entity thrower, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy);

  /**
   * Retrieves the thrower of the entity.
   *
   * @return The entity thrower.
   */
  LivingEntity getThrower();

  /** A factory class for the {@link ThrowableEntity}. */
  @AssistedFactory(ThrowableEntity.class)
  interface Factory {

    /**
     * Creates a new {@link ThrowableEntity} with the given parameters.
     *
     * @param entity The throwable entity.
     * @param entityType The type of the throwable entity.
     * @return A created throwable entity.
     */
    ThrowableEntity create(
        @Assisted("entity") Object entity, @Assisted("entityType") EntityType entityType);

    /**
     * Creates a new {@link ThrowableEntity} with the given parameters.
     *
     * @param entity The entity.
     * @param x The x position.
     * @param y The y position.
     * @param z The z position.
     * @return A created throwable entity.
     */
    ThrowableEntity create(
        @Assisted("entity") Object entity,
        @Assisted("entityType") EntityType entityType,
        @Assisted("x") double x,
        @Assisted("y") double y,
        @Assisted("z") double z);

    /**
     * Creates a new {@link ThrowableEntity} with the given parameters.
     *
     * @param entity The entity.
     * @param entityType The type of the entity.
     * @param thrower The thrower of the entity.
     * @return A created throwable entity.
     */
    ThrowableEntity create(
        @Assisted("entity") Object entity,
        @Assisted("entityType") EntityType entityType,
        @Assisted("thrower") LivingEntity thrower);
  }
}
