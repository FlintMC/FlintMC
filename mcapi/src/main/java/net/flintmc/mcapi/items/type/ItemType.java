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

package net.flintmc.mcapi.items.type;

import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.items.ItemRegistry;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.meta.ItemMeta;
import net.flintmc.mcapi.resources.ResourceLocation;

/**
 * Represents a unique type of an item.
 */
public interface ItemType {

  /**
   * Retrieves the category of this type where the item will be displayed in the creative menu. If
   * this type isn't displayed in the creative menu, this will be {@code null}.
   *
   * @return The category or {@code null} if it is not available
   */
  ItemCategory getCategory();

  /**
   * Retrieves the name of this type which is being used to identify this type. The name is unique
   * per {@link ItemRegistry}.
   *
   * @return The non-null registry name
   */
  NameSpacedKey getRegistryName();

  /**
   * Retrieves the default display name of this type which will be displayed when no custom display
   * name has been set for the item.
   *
   * @return The non-null default display name of this type
   */
  ChatComponent getDefaultDisplayName();

  /**
   * Retrieves the max size per stack of items of this type that can be created without modification
   * of the client/server. The number of items of a stack might be bigger than that if the stack
   * hasn't been created by a player.
   *
   * @return The max stack size of this item, always greater than zero.
   */
  int getMaxStackSize();

  /**
   * Retrieves the max damage that can be done to items of this type
   *
   * @return The max damage of this type or -1 if this type is not damageable
   * @see #isDamageable()
   */
  int getMaxDamage();

  /**
   * Retrieves whether this type of items can be damaged (e.g. sword, pickaxe) or not (e.g. building
   * blocks, redstone).
   *
   * @return Whether this type is damageable or not
   */
  boolean isDamageable();

  /**
   * Retrieves the class that will be used to create the item meta of item stacks with this type.
   *
   * @return The non-null class for the item meta of stacks of this type
   */
  Class<? extends ItemMeta> getMetaClass();

  /**
   * Creates a new stack of items of this type with a stack size of 1. The retrieved stack doesn't
   * have an {@link ItemMeta} by default
   *
   * @return The new non-null stack of items with this type
   */
  ItemStack createStack();

  /**
   * Creates a new stack of items of this type. The retrieved stack doesn't have an {@link ItemMeta}
   * by default
   *
   * @param stackSize The size of the stack
   * @return The new non-null stack of items with this type
   */
  ItemStack createStack(int stackSize);

  /**
   * Retrieves the resource location for the texture of this type of item.
   *
   * @return The non-null resource location pointing to the texture of this type
   */
  ResourceLocation getResourceLocation();

  /**
   * Builder for {@link ItemType}.
   */
  interface Builder {

    /**
     * Sets the category of the result of this builder. If no category has been provided, the result
     * type will not be displayed in any creative inventory.
     *
     * @param category The category of this builder or {@code null} to not display the result type
     *                 in the creative menu
     * @return this
     */
    Builder category(ItemCategory category);

    /**
     * Sets the registry name of the result of this builder. This name is unique per {@link
     * ItemRegistry}.
     *
     * @param registryName The non-null registry name of the result type
     * @return this
     */
    Builder registryName(NameSpacedKey registryName);

    /**
     * Sets the default display name of the result of this builder to be displayed when no other
     * display name is set to an {@link ItemStack}.
     *
     * @param defaultDisplayName The non-null default display name of the result type
     * @return this
     */
    Builder defaultDisplayName(ChatComponent defaultDisplayName);

    /**
     * Sets the maximum stack size of the result of this builder.
     *
     * @param maxStackSize The maximum stack size of the result type, needs to be greater than zero
     * @return this
     */
    Builder maxStackSize(int maxStackSize);

    /**
     * Sets the maximum damage of the result of this builder.
     *
     * @param maxDamage The maximum damage of the result type or -1 if the result type should not be
     *                  damageable.
     * @return this
     */
    Builder maxDamage(int maxDamage);

    /**
     * Sets the class that will be used to instantiate a new {@link ItemMeta} for the stacks of this
     * type. The given class needs an associated instance in the guice injector of the project,
     * otherwise an exception will be thrown when trying to create the meta.
     *
     * @param metaClass The non-null class for the meta of the result type
     * @return this
     */
    Builder metaClass(Class<? extends ItemMeta> metaClass);

    /**
     * Sets the resource location for the texture of the result of this builder.
     *
     * @param resourceLocation The non-null resource location to the texture of the result type
     * @return this
     */
    Builder resourceLocation(ResourceLocation resourceLocation);

    /**
     * Builds a new {@link ItemType} with the parameters specified in this builder.
     *
     * <p>The only necessary parameter to be specified is {@link #registryName(NameSpacedKey)}.
     *
     * <p>If no {@link #defaultDisplayName(ChatComponent)} has been provided, a new {@link
     * TextComponent} will be used with the {@link #registryName(NameSpacedKey)}. If no {@link
     * #resourceLocation(ResourceLocation)} has been provided, a new {@link ResourceLocation} will
     * be created with the given {@link #registryName(NameSpacedKey)}.
     *
     * @return The new non-null type of items
     * @throws IllegalArgumentException If the specified {@link #maxStackSize(int)} is smaller than
     *                                  or equal to zero
     * @throws NullPointerException     If no {@link #registryName(NameSpacedKey)} (or {@code null})
     *                                  has been provided
     * @throws NullPointerException     If {@code null} has been provided as the {@link
     *                                  #metaClass(Class)}
     */
    ItemType build() throws IllegalArgumentException;

    /**
     * Resets all values in this builder to their default as if you were creating a new builder.
     */
    void reset();
  }

  /**
   * Factory for the {@link Builder} for {@link ItemType}s.
   */
  @AssistedFactory(Builder.class)
  interface Factory {

    /**
     * Creates a new builder for a new {@link ItemType}.
     *
     * @return The new non-null builder
     */
    Builder newBuilder();
  }
}
