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

package net.flintmc.mcapi.potion;

import java.util.Map;
import net.flintmc.mcapi.potion.effect.StatusEffect;
import net.flintmc.mcapi.resources.ResourceLocation;

/**
 * Represents a potion register to registers potions or effects.
 */
public interface PotionRegister {

  /**
   * Adds a new {@link StatusEffect} to the register.
   *
   * @param location The location of the status effect.
   * @param effect   The status effect to be added.
   */
  void addEffect(ResourceLocation location, StatusEffect effect);

  /**
   * Removes a status effect from the register.
   *
   * @param location The location of the status effect.
   */
  void removeEffect(ResourceLocation location);

  /**
   * Removes a status effect from the register.
   *
   * @param location The location of the status effect.
   * @param effect   The effect to be removed.
   */
  void removeEffect(ResourceLocation location, StatusEffect effect);

  /**
   * Retrieves a status effect with the given {@code location}.
   *
   * @param location The location of the status effect.
   * @return A status effect or {@code null}.
   */
  StatusEffect getEffect(ResourceLocation location);

  /**
   * Retrieves a key-value system with the registered {@link StatusEffect}'s.
   *
   * @return A key-value system.
   */
  Map<ResourceLocation, StatusEffect> getEffects();

  /**
   * Adds a new {@link Potion} to the register.
   *
   * @param location The location of the potion.
   * @param potion   The potion to be added.
   */
  void addPotion(ResourceLocation location, Potion potion);

  /**
   * Removes a potion from the register.
   *
   * @param location The location of the potion.
   */
  void removePotion(ResourceLocation location);

  /**
   * Removes a potion from the register.
   *
   * @param location The location of the potion.
   * @param potion   The potion to be removed.
   */
  void removePotion(ResourceLocation location, Potion potion);

  /**
   * Retrieves a potion with the given {@code location}.
   *
   * @param location The location of the potion.
   * @return A potion with the location or {@code null}.
   */
  Potion getPotion(ResourceLocation location);

  /**
   * Retrieves a key-value system with the registered {@link Potion}'s.
   *
   * @return A key-value system.
   */
  Map<ResourceLocation, Potion> getPotions();
}
