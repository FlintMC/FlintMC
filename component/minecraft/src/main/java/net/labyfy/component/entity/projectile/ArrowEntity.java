package net.labyfy.component.entity.projectile;

import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.player.type.sound.Sound;

/**
 * Represents the Minecraft arrow entity.
 */
public interface ArrowEntity extends Entity, Projectile {

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

  void setKnockbackStrength(int knockbackStrength);

  boolean isCritical();

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

  void setEnchantmentEffectsFromEntity(LivingEntity entity, float damage);

  boolean isNoClip();

  void setNoClip(boolean noClip);

  void setShotFromCrossbow(boolean fromCrossbow);

}
