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

package net.flintmc.mcapi.entity.item;

import java.util.UUID;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.items.ItemStack;

/**
 * Represents the Minecraft item entity.
 */
public interface ItemEntity extends Entity {

  /**
   * Retrieves the entity as an item stack.
   *
   * @return The entity as an item stack.
   */
  ItemStack getItemStack();

  /**
   * Changes the item stack of this entity.
   *
   * @param itemStack The new item stack.
   */
  void setItemStack(ItemStack itemStack);

  /**
   * Retrieves the identifier of the item entity owner.
   *
   * @return The identifier of the item entity owner.
   */
  UUID getOwnerIdentifier();

  /**
   * Changes the owner identifier.
   *
   * @param ownerIdentifier The new owner identifier.
   */
  void setOwnerIdentifier(UUID ownerIdentifier);

  /**
   * Retrieves the identifier of the item entity thrower.
   *
   * @return The identifier of the item entity thrower.
   */
  UUID getThrowerIdentifier();

  /**
   * Changes the thrower identifier.
   *
   * @param throwerIdentifier The new thrower identifier.
   */
  void setThrowerIdentifier(UUID throwerIdentifier);

  /**
   * Retrieves the age of this item entity.
   *
   * @return The age of this item entity.
   */
  int getAge();

  /**
   * Sets that the item entity has a default pickup delay.
   */
  void setDefaultPickupDelay();

  /**
   * Sets that the item entity has not pickup delay.
   */
  void setNoPickupDelay();

  /**
   * Sets that the item entity has an infinite pickup delay.
   */
  void setInfinitePickupDelay();

  /**
   * Changes the pickup delay of this item entity.
   *
   * @param ticks The new pickup delay.
   */
  void setPickupDelay(int ticks);

  /**
   * Whether the item entity cannot be picked up.
   *
   * @return {@code true} if the item entity cannot be picked up, otherwise {@code false}.
   */
  boolean cannotPickup();

  /**
   * Sets that the item cannot despawn.
   */
  void setNoDespawn();

  /**
   * Make the entity a fake item.
   */
  void makeFakeItem();

  /**
   * A factory class for the {@link ItemEntity}.
   */
  @AssistedFactory(ItemEntity.class)
  interface Factory {

    /**
     * Creates a new {@link ItemEntity} with the given entity.
     *
     * @param entity The entity.
     * @return A created item entity.
     */
    ItemEntity create(@Assisted("entity") Object entity);

    /**
     * Creates a new {@link ItemEntity} with the given parameters.
     *
     * @param entity The entity.
     * @param x      The x position of the entity.
     * @param y      The y position of the entity.
     * @param z      The z position of the entity.
     * @return A created item entity.
     */
    ItemEntity create(
        @Assisted("entity") Object entity,
        @Assisted("x") double x,
        @Assisted("y") double y,
        @Assisted("z") double z);

    /**
     * Creates a new {@link ItemEntity} with the given parameters.
     *
     * @param entity    The entity.
     * @param x         The x position of the entity.
     * @param y         The y position of the entity.
     * @param z         The z position of the entity.
     * @param itemStack The item stack for the entity.
     * @return A created item entity.
     */
    ItemEntity create(
        @Assisted("entity") Object entity,
        @Assisted("x") double x,
        @Assisted("y") double y,
        @Assisted("z") double z,
        @Assisted("itemStack") ItemStack itemStack);
  }
}
