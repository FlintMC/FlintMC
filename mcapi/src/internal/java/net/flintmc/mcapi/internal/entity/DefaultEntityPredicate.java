/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.internal.entity;

import java.util.function.Predicate;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.EntityPredicate;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.MobEntity;

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
          double visibilityModifier =
              this.useVisibilityModifier ? target.getVisibilityMultiplier(attacker) : 1.0D;
          double maxDistance = this.distance * visibilityModifier;
          double distanceSq =
              attacker.getDistanceSq(target.getPosX(), target.getPosY(), target.getPosZ());

          if (distanceSq > maxDistance * maxDistance) {
            return false;
          }
        }

        return this.requireLineOfSight
            || !(attacker instanceof MobEntity)
            || ((MobEntity) attacker).getEntitySenses().canSeeEntity(target);
      }

      return true;
    }
  }
}
