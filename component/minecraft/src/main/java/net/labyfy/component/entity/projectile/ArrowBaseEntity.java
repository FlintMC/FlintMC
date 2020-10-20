package net.labyfy.component.entity.projectile;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.entity.projectile.type.PickupStatus;
import net.labyfy.component.entity.projectile.type.Projectile;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.player.type.sound.Sound;

/**
 * Represents the Minecraft arrow entity.
 */
public interface ArrowBaseEntity extends Entity, Projectile {

  /**
   * Retrieves the hit sound of this arrow entity.
   *
   * @return The hit sound.
   */
  Sound getHitSound();

  /**
   * Changes the hit sound of this arrow entity.
   *
   * @param sound The new hit sound for this arrow entity.
   */
  void setHitSound(Sound sound);

  /**
   * Shoots the arrow.
   *
   * @param shooter     The shooter of the arrow.
   * @param pitch       The pitch for the arrow.
   * @param yaw         The yaw for the arrow.
   * @param pitchOffset The pitch offset for the arrow.
   * @param velocity    The velocity for the arrow.
   * @param inaccuracy  The inaccuracy for the arrow.
   * @see Projectile#shoot(double, double, double, float, float)
   */
  void shoot(Entity shooter, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy);

  /**
   * Retrieves the shooter of this arrow entity.
   *
   * @return The arrow entity shooter.
   */
  Entity getShooter();

  /**
   * Changes the shooter of this arrow entity.
   *
   * @param shooter The new shooter.
   */
  void setShooter(Entity shooter);

  /**
   * Retrieves the arrow as an {@link ItemStack}.
   *
   * @return The arrow as an item stack.
   */
  ItemStack getArrowStack();

  /**
   * Retrieves the damage of this arrow entity.
   *
   * @return The arrow entity damage.
   */
  double getDamage();

  /**
   * Changes the damage of this arrow entity.
   *
   * @param damage The new damage for the arrow entity.
   */
  void setDamage(double damage);

  /**
   * Retrieves the knockback strength of the arrow.
   *
   * @return The knockback strength.
   */
  int getKnockbackStrength();

  /**
   * Changes the knockback strength of the arrow.
   *
   * @param knockbackStrength The new strength.
   */
  void setKnockbackStrength(int knockbackStrength);

  /**
   * Whether the arrow is a critical hit.
   *
   * @return {@code true} if the arrow is a critical hit, otherwise {@code false}.
   */
  boolean isCritical();

  /**
   * Changes whether the arrow is a critical hit.
   *
   * @param critical {@code true} if the arrow should a critical hit, otherwise {@code false}.
   */
  void setCritical(boolean critical);

  /**
   * Retrieves the pierce level of this arrow entity.
   *
   * @return The pierce level.
   */
  byte getPierceLevel();

  /**
   * Changes the pierce level of this arrow entity.
   *
   * @param level The new pierce level.
   */
  void setPierceLevel(byte level);

  /**
   * Changes the enchantment effects from the given entity.
   *
   * @param entity The entity to sets the enchantment effects.
   * @param damage The enchantment damage.
   */
  void setEnchantmentEffectsFromEntity(LivingEntity entity, float damage);

  /**
   * Whether the arrow is no clip.
   *
   * @return {@code true} if the arrow is no clip, otherwise {@code false}.
   */
  boolean isNoClip();

  /**
   * Changes whether the arrow is no clip.
   *
   * @param noClip {@code true} if the arrow should be no clip, otherwise {@code false}.
   */
  void setNoClip(boolean noClip);

  /**
   * Whether the arrow is shot from the crossbow.
   *
   * @return {@code true} if the arrow is shot from the crossbow, otherwise {@code false}.
   */
  boolean isShotFromCrossbow();

  /**
   * Changes whether the arrow is shot from the crossbow.
   *
   * @param fromCrossbow {@code true} if the arrow should be shot from the crossbow, otherwise {@code false}.
   */
  void setShotFromCrossbow(boolean fromCrossbow);

  /**
   * Retrieves the water drag of the arrow.
   *
   * @return The arrow water drag.
   */
  float getWaterDrag();

  /**
   * Retrieves the pickup status of the arrow.
   *
   * @return The arrow pickup status.
   */
  PickupStatus getPickupStatus();

  /**
   * Changes the pickup status of the arrow.
   *
   * @param pickupStatus The new pickup status.
   */
  void setPickupStatus(PickupStatus pickupStatus);

  /**
   * A factory class for the {@link ArrowBaseEntity}.
   */
  @AssistedFactory(ArrowBaseEntity.class)
  interface Factory {

    /**
     * Creates a new {@link ArrowBaseEntity} with the given entity.
     *
     * @param entity The entity.
     * @return A created arrow base entity.
     */
    ArrowBaseEntity create(@Assisted("entity") Object entity);

    /**
     * Creates a new {@link ArrowBaseEntity} with the given parameters.
     *
     * @param entity The entity.
     * @param x      The x position.
     * @param y      The y position.
     * @param z      The z position.
     * @return A created arrow base entity.
     */
    ArrowBaseEntity create(
            @Assisted("entity") Object entity,
            @Assisted("x") double x,
            @Assisted("y") double y,
            @Assisted("z") double z
    );

    /**
     * Creates a new {@link ArrowBaseEntity} with the given parameters.
     *
     * @param entity  The entity.
     * @param shooter The shooter of the arrow.
     * @return A created arrow base entity.
     */
    ArrowBaseEntity create(
            @Assisted("entity") Object entity,
            @Assisted("shooter") LivingEntity shooter
    );

  }

}
