package net.labyfy.component.entity.projectile;

import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.LivingEntity;

/**
 * Represents the Minecraft throwable entity.
 */
public interface ThrowableEntity extends Entity, Projectile {

  void shoot(Entity thrower, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy);

  LivingEntity getThrower();


}

