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

package net.flintmc.mcapi.internal.potion;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.potion.Potion;
import net.flintmc.mcapi.potion.PotionRegister;
import net.flintmc.mcapi.potion.effect.StatusEffect;
import net.flintmc.mcapi.resources.ResourceLocation;

@Singleton
@Implement(PotionRegister.class)
public class DefaultPotionRegister implements PotionRegister {

  private final Map<ResourceLocation, StatusEffect> effects;
  private final Map<ResourceLocation, Potion> potions;

  @Inject
  private DefaultPotionRegister() {
    this.effects = new HashMap<>();
    this.potions = new HashMap<>();
  }

  /** {@inheritDoc} */
  @Override
  public void addEffect(ResourceLocation location, StatusEffect effect) {
    this.effects.put(location, effect);
  }

  /** {@inheritDoc} */
  @Override
  public void removeEffect(ResourceLocation location) {
    this.effects.remove(location);
  }

  /** {@inheritDoc} */
  @Override
  public void removeEffect(ResourceLocation location, StatusEffect effect) {
    this.effects.remove(location, effect);
  }

  /** {@inheritDoc} */
  @Override
  public StatusEffect getEffect(ResourceLocation location) {
    return this.effects.get(location);
  }

  /** {@inheritDoc} */
  @Override
  public Map<ResourceLocation, StatusEffect> getEffects() {
    return this.effects;
  }

  /** {@inheritDoc} */
  @Override
  public void addPotion(ResourceLocation location, Potion potion) {
    this.potions.put(location, potion);
  }

  /** {@inheritDoc} */
  @Override
  public void removePotion(ResourceLocation location) {
    this.potions.remove(location);
  }

  /** {@inheritDoc} */
  @Override
  public void removePotion(ResourceLocation location, Potion potion) {
    this.potions.remove(location, potion);
  }

  /** {@inheritDoc} */
  @Override
  public Potion getPotion(ResourceLocation location) {
    return this.potions.get(location);
  }

  /** {@inheritDoc} */
  @Override
  public Map<ResourceLocation, Potion> getPotions() {
    return this.potions;
  }
}
