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

package net.flintmc.mcapi.potion.mapper;

import net.flintmc.mcapi.potion.Potion;
import net.flintmc.mcapi.potion.effect.StatusEffect;
import net.flintmc.mcapi.potion.effect.StatusEffectInstance;

/**
 * Mapper between Minecraft potion and Flint potion.
 */
public interface PotionMapper {

  /**
   * Creates a new Minecraft potion by using the Flint {@link Potion} as the base.
   *
   * @param potion The non-null Flint {@link Potion}.
   * @return The new Minecraft potion or the default potion if the given potion was invalid.
   */
  Object toMinecraftPotion(Potion potion);

  /**
   * Creates a new {@link Potion} by using the given Minecraft potion as the base.
   *
   * @param handle The non-null Minecraft potion.
   * @return The new Flint {@link Potion} or {@code null} if the given potion was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft potion.
   */
  Potion fromMinecraftPotion(Object handle);

  /**
   * Creates a new Minecraft effect by using the Flint {@link StatusEffect} as the base.
   *
   * @param statusEffect The non-null Flint {@link StatusEffect}.
   * @return The new Minecraft effect or the default effect if the given effect was invalid.
   */
  Object toMinecraftEffect(StatusEffect statusEffect);

  /**
   * Creates a new {@link StatusEffect} by using the given Minecraft effect as the base.
   *
   * @param handle The non-null Minecraft effect.
   * @return The new Flint {@link StatusEffect} or {@code null} if the given effect was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft effect.
   */
  StatusEffect fromMinecraftEffect(Object handle);

  /**
   * Creates a new Minecraft effect by using the Flint {@link StatusEffect.Type} as the base.
   *
   * @param effectType The non-null Flint {@link StatusEffect.Type}.
   * @return The new Minecraft effect or the default effect if the given effect type was invalid.
   */
  Object toMinecraftEffectType(StatusEffect.Type effectType);

  /**
   * Creates a new {@link StatusEffect.Type} by using the given Minecraft effect type as the base.
   *
   * @param handle The non-null Minecraft effect type.
   * @return The new Flint {@link StatusEffect.Type}.
   */
  StatusEffect.Type fromMinecraftEffectType(Object handle);

  /**
   * Creates a new Minecraft effect instance by using the Flint {@link StatusEffectInstance} as the
   * base.
   *
   * @param foundation The non-null Flint {@link StatusEffectInstance}.
   * @return The new Minecraft effect instance.
   */
  Object toMinecraftEffectInstance(StatusEffectInstance foundation);

  /**
   * Creates a new {@link StatusEffectInstance} by using the given Minecraft effect instance as the
   * base.
   *
   * @param handle The non-null Minecraft effect instance.
   * @return The new Flint {@link StatusEffectInstance} or {@code null} if the given effect instance
   * was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft effect instance.
   */
  StatusEffectInstance fromMinecraftEffectInstance(Object handle);

  /**
   * Creates a new array of Minecraft effect instances by using the {@link StatusEffectInstance} as
   * the base.
   *
   * @param effects The non-null Flint array of {@link StatusEffectInstance}.
   * @return The new array of Minecraft effect instances.
   */
  Object[] toMinecraftEffectInstanceArray(StatusEffectInstance... effects);
}
