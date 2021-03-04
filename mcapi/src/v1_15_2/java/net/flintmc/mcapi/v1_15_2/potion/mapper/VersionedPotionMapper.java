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

package net.flintmc.mcapi.v1_15_2.potion.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.potion.Potion;
import net.flintmc.mcapi.potion.effect.StatusEffect;
import net.flintmc.mcapi.potion.effect.StatusEffectInstance;
import net.flintmc.mcapi.potion.mapper.PotionMapper;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

/**
 * 1.15.2 implementation of the {@link PotionMapper}.
 */
@Singleton
@Implement(value = PotionMapper.class)
public class VersionedPotionMapper implements PotionMapper {

  private final StatusEffect.Factory effectFactory;
  private final StatusEffectInstance.Factory effectFoundationFactory;
  private final Potion.Factory potionFactory;

  @Inject
  private VersionedPotionMapper(
      StatusEffect.Factory effectFactory,
      StatusEffectInstance.Factory effectFoundationFactory,
      Potion.Factory potionFactory) {
    this.effectFactory = effectFactory;
    this.effectFoundationFactory = effectFoundationFactory;
    this.potionFactory = potionFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftPotion(Potion potion) {
    return net.minecraft.potion.Potion.getPotionTypeForName(potion.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Potion fromMinecraftPotion(Object handle) {
    if (!(handle instanceof net.minecraft.potion.Potion)) {
      throw new IllegalArgumentException(
          handle.getClass().getName()
              + " is not an instance of "
              + net.minecraft.potion.Potion.class.getName());
    }

    net.minecraft.potion.Potion potion = (net.minecraft.potion.Potion) handle;

    StatusEffectInstance[] effects = new StatusEffectInstance[potion.getEffects().size()];

    for (int i = 0; i < potion.getEffects().size(); i++) {
      effects[i] = this.fromMinecraftEffectInstance(potion.getEffects().get(i));
    }

    String name = Registry.POTION.getKey(potion).getPath();

    return name == null
        ? this.potionFactory.create(effects)
        : this.potionFactory.create(name, effects);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftEffect(StatusEffect statusEffect) {
    return this.getEffect(statusEffect.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StatusEffect fromMinecraftEffect(Object handle) {
    if (!(handle instanceof net.minecraft.potion.Effect)) {
      throw new IllegalArgumentException(
          handle.getClass().getName()
              + " is not an instance of "
              + net.minecraft.potion.Effect.class.getName());
    }

    net.minecraft.potion.Effect effect = (net.minecraft.potion.Effect) handle;

    return this.effectFactory.create(
        effect.getName(),
        effect.isInstant(),
        this.fromMinecraftEffectType(effect.getEffectType()),
        effect.getLiquidColor());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftEffectType(StatusEffect.Type effectType) {
    switch (effectType) {
      case BENEFICIAL:
        return EffectType.BENEFICIAL;
      case HARMFUL:
        return EffectType.HARMFUL;
      default:
        return EffectType.NEUTRAL;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StatusEffect.Type fromMinecraftEffectType(Object handle) {

    EffectType effectType = (EffectType) handle;

    switch (effectType) {
      case BENEFICIAL:
        return StatusEffect.Type.BENEFICIAL;
      case HARMFUL:
        return StatusEffect.Type.HARMFUL;
      default:
        return StatusEffect.Type.NEUTRAL;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftEffectInstance(StatusEffectInstance foundation) {
    return new EffectInstance(
        (net.minecraft.potion.Effect) this.toMinecraftEffect(foundation.getPotion()),
        foundation.getDuration(),
        foundation.getAmplifier(),
        foundation.isAmbient(),
        foundation.doesShowParticles());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StatusEffectInstance fromMinecraftEffectInstance(Object handle) {
    if (!(handle instanceof EffectInstance)) {
      throw new IllegalArgumentException(
          handle.getClass().getName() + " is not an instance of " + EffectInstance.class.getName());
    }

    EffectInstance effectInstance = (EffectInstance) handle;

    return this.effectFoundationFactory.create(
        this.fromMinecraftEffect(effectInstance.getPotion()),
        effectInstance.getDuration(),
        effectInstance.getAmplifier(),
        effectInstance.isAmbient(),
        effectInstance.getIsPotionDurationMax(),
        effectInstance.doesShowParticles(),
        effectInstance.isShowIcon());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object[] toMinecraftEffectInstanceArray(StatusEffectInstance... effects) {

    EffectInstance[] effectInstances = new EffectInstance[effects.length];

    for (int i = 0; i < effectInstances.length; i++) {
      effectInstances[i] = (EffectInstance) this.toMinecraftEffectInstance(effects[i]);
    }

    return effectInstances;
  }

  private net.minecraft.potion.Effect getEffect(String name) {
    return Registry.EFFECTS.getOrDefault(ResourceLocation.tryCreate(name));
  }
}
