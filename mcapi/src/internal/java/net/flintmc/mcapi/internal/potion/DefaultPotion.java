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

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nullable;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.potion.Potion;
import net.flintmc.mcapi.potion.effect.StatusEffectInstance;

@Implement(Potion.class)
public class DefaultPotion implements Potion {

  private final String name;
  private final List<StatusEffectInstance> effects;

  @AssistedInject
  public DefaultPotion(@Assisted("effects") StatusEffectInstance... effects) {
    this(null, effects);
  }

  @AssistedInject
  public DefaultPotion(
      @Assisted("name") @Nullable String name,
      @Assisted("effects") StatusEffectInstance... effects) {
    this.name = name;
    this.effects = ImmutableList.copyOf(effects);
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return this.name;
  }

  /** {@inheritDoc} */
  @Override
  public List<StatusEffectInstance> getStatusEffects() {
    return this.effects;
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasInstantly() {
    return !this.effects.isEmpty()
        && this.effects.stream().anyMatch(effect -> effect.getPotion().isInstant());
  }
}
