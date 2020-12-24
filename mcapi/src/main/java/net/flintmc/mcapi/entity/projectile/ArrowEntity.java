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

package net.flintmc.mcapi.entity.projectile;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.items.ItemStack;

/**
 * Represents the Minecraft arrow entity.
 */
public interface ArrowEntity extends ArrowBaseEntity {

  /**
   * Changes the potion effect of the arrow.
   *
   * @param itemStack The item stack to change the potion effect.
   */
  void setPotionEffect(ItemStack itemStack);

  /**
   * Retrieves the arrow color.
   *
   * @return The arrow color.
   */
  int getColor();

  /**
   * A factory class for the {@link ArrowBaseEntity}.
   */
  @AssistedFactory(ArrowEntity.class)
  interface Factory {

    /**
     * Creates a new {@link ArrowEntity} with the given entity.
     *
     * @param entity The entity.
     * @return A created arrow base entity.
     */
    ArrowEntity create(@Assisted("entity") Object entity);

    /**
     * Creates a new {@link ArrowEntity} with the given parameters.
     *
     * @param entity The entity.
     * @param x      The x position.
     * @param y      The y position.
     * @param z      The z position.
     * @return A created arrow base entity.
     */
    ArrowEntity create(
        @Assisted("entity") Object entity,
        @Assisted("x") double x,
        @Assisted("y") double y,
        @Assisted("z") double z);

    /**
     * Creates a new {@link ArrowEntity} with the given parameters.
     *
     * @param entity  The entity.
     * @param shooter The shooter of the arrow.
     * @return A created arrow base entity.
     */
    ArrowEntity create(
        @Assisted("entity") Object entity, @Assisted("shooter") LivingEntity shooter);
  }
}
