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

package net.flintmc.util.attribute;

import java.util.Collection;
import java.util.function.Consumer;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents an instance of an attribute and its modifiers.
 */
public interface AttributeInstance {

  /**
   * Retrieves the attribute associated to this instance.
   *
   * @return The associated attribute.
   */
  Attribute getAttribute();

  /**
   * Retrieves the base value of this instance without modifiers.
   *
   * @return The instance's base value.
   * @see #setBaseValue(float)
   */
  float getBaseValue();

  /**
   * Changes the base value of this instance.
   *
   * @param value The new base value.
   */
  void setBaseValue(float value);

  /**
   * Adds a modifier to this instance.
   *
   * @param modifier The modifier to be added.
   */
  void addModifier(AttributeModifier modifier);

  /**
   * Removes a modifier from this instance.
   *
   * @param modifier The modifier to be removed.
   */
  void removeModifier(AttributeModifier modifier);

  /**
   * Retrieves a collection of modifiers applied to this instance.
   *
   * @return A collection of modifiers.
   */
  Collection<AttributeModifier> getModifiers();

  /**
   * Retrieves the value of this attribute instance using the modifiers.
   *
   * @return The attribute value.
   */
  float getValue();

  @AssistedFactory(AttributeInstance.class)
  interface Factory {

    /**
     * Creates a new {@link AttributeInstance} with the {@code attribute} and the {@code
     * propertyChangeListener}.
     *
     * @param attribute              The attribute for the instance.
     * @param propertyChangeListener The property change listener for the instance.
     * @return A created attribute instance.
     */
    AttributeInstance create(
        @Assisted("attribute") Attribute attribute,
        @Assisted("propertyChangeListener") Consumer<AttributeInstance> propertyChangeListener);
  }
}
