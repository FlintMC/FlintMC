package net.flintmc.mcapi.internal.entity;

import net.flintmc.mcapi.entity.EntityPredicate;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.MobEntity;
import net.flintmc.framework.inject.implement.Implement;

import java.util.function.Predicate;

/**
 * Default implementation of the {@link EntityPredicate}.
 */
@Implement(EntityPredicate.class)
public class DefaultEntityPredicate implements EntityPredicate {

  private double distance;
  private boolean allowInvulnerable;
  private boolean friendlyFire;
  private boolean requireLineOfSight;
  private boolean skipAttackChecks;
  private boolean useVisibilityModifier;
  private Predicate<LivingEntity> customPredicate;

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityPredicate setDistance(double distance) {
    this.distance = distance;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityPredicate allowInvulnerable() {
    this.allowInvulnerable = true;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityPredicate allowFriendlyFire() {
    this.friendlyFire = true;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityPredicate allowRequireLineOfSight() {
    this.requireLineOfSight = true;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityPredicate allowSkipAttackChecks() {
    this.skipAttackChecks = true;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityPredicate disallowInvisibilityCheck() {
    this.useVisibilityModifier = false;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityPredicate setCustomPredicate(Predicate<LivingEntity> predicate) {
    this.customPredicate = predicate;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canTarget(LivingEntity attacker, LivingEntity target) {
    if (attacker == target) {
      return false;
    } else if (target.isSpectator()) {
      return false;
    } else if (!target.isAlive()) {
      return false;
    } else if (!this.allowInvulnerable && target.isInvulnerable()) {
      return false;
    } else if (this.customPredicate != null && !this.customPredicate.test(target)) {
      return false;
    } else {

      if (attacker != null) {
        if (!this.skipAttackChecks) {
          if (!attacker.canAttack(target)) {
            return false;
          }

          if (!attacker.canAttack(target.getType())) {
            return false;
          }
        }

        if (!this.friendlyFire && attacker.isInSameTeam(target)) {
          return false;
        }

        if (this.distance > 0) {
          double visibilityModifier = this.useVisibilityModifier ? target.getVisibilityMultiplier(attacker) : 1.0D;
          double maxDistance = this.distance * visibilityModifier;
          double distanceSq = attacker.getDistanceSq(target.getPosX(), target.getPosY(), target.getPosZ());

          if (distanceSq > maxDistance * maxDistance) {
            return false;
          }
        }

        return this.requireLineOfSight || !(attacker instanceof MobEntity) || ((MobEntity) attacker).getEntitySenses().canSeeEntity(target);
      }

      return true;
    }
  }
}
