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

package net.flintmc.mcapi.entity;

import java.util.function.Predicate;

/**
 * Represents an entity predicate.
 */
public interface EntityPredicate {

  /**
   * Changes the distance of this entity predicate.
   *
   * @param distance The new distance.
   * @return This predicate, for chaining.
   */
  EntityPredicate setDistance(double distance);

  /**
   * Allows the predicate to attack invulnerable entities.
   *
   * @return This predicate, for chaining.
   */
  EntityPredicate allowInvulnerable();

  /**
   * Allows the predicate to attack friendly entities.
   *
   * @return This predicate, for chaining.
   */
  EntityPredicate allowFriendlyFire();

  /**
   * Allows the predicate to requires line of sight.
   *
   * @return This predicate, for chaining.
   */
  EntityPredicate allowRequireLineOfSight();

  /**
   * Allows the predicate to skip the attack checks.
   *
   * @return This predicate, for chaining.
   */
  EntityPredicate allowSkipAttackChecks();

  /**
   * Disallows the predicate to checks the invisibility.
   *
   * @return This predicate, for chaining.
   */
  EntityPredicate disallowInvisibilityCheck();

  /**
   * @param predicate The new predicate.
   * @return This predicate, for chaining.
   */
  EntityPredicate setCustomPredicate(Predicate<LivingEntity> predicate);

  /**
   * Whether the {@code attacker} can attack the {@code target} entity.
   *
   * @param attacker The entity which wants to attack.
   * @param target   The target which is to be attacked.
   * @return {@code true} if the {@code attacker} can attack the {@code target} entity, otherwise
   * {@code false}.
   */
  boolean canTarget(LivingEntity attacker, LivingEntity target);
}
