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

package net.flintmc.mcapi.internal.potion.status;

import java.util.Objects;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.potion.effect.StatusEffect;

@Implement(StatusEffect.class)
public class DefaultStatusEffect implements StatusEffect {

  private final String name;
  private final boolean instant;
  private final StatusEffect.Type statusEffectType;
  private final int liquidColor;

  @AssistedInject
  public DefaultStatusEffect(
      @Assisted("name") String name,
      @Assisted("instant") boolean instant,
      @Assisted("effectType") Type statusEffectType,
      @Assisted("liquidColor") int liquidColor) {
    this.name = name;
    this.instant = instant;
    this.statusEffectType = statusEffectType;
    this.liquidColor = liquidColor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isBeneficial() {
    return this.statusEffectType == Type.BENEFICIAL;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getLiquidColor() {
    return this.liquidColor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Type getEffectType() {
    return this.statusEffectType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInstant() {
    return this.instant;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DefaultStatusEffect that = (DefaultStatusEffect) o;
    return instant == that.instant &&
        liquidColor == that.liquidColor &&
        Objects.equals(name, that.name) &&
        statusEffectType == that.statusEffectType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, instant, statusEffectType, liquidColor);
  }
}
