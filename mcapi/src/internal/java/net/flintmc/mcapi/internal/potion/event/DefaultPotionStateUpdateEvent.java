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

package net.flintmc.mcapi.internal.potion.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.potion.effect.StatusEffectInstance;
import net.flintmc.mcapi.potion.event.PotionStateUpdateEvent;

@Implement(PotionStateUpdateEvent.class)
public class DefaultPotionStateUpdateEvent implements PotionStateUpdateEvent {

  private final LivingEntity livingEntity;
  private final StatusEffectInstance statusEffectInstance;
  private final State state;

  @AssistedInject
  public DefaultPotionStateUpdateEvent(
      @Assisted("livingEntity") LivingEntity livingEntity,
      @Assisted("effect") StatusEffectInstance effect,
      @Assisted("state") State state) {
    this.livingEntity = livingEntity;
    this.statusEffectInstance = effect;
    this.state = state;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LivingEntity getLivingEntity() {
    return this.livingEntity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StatusEffectInstance getStatusEffectInstance() {
    return this.statusEffectInstance;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public State getState() {
    return this.state;
  }
}
