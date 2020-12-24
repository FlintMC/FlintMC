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

import java.util.UUID;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents an attribute modifier.
 */
public interface AttributeModifier {

  /**
   * Retrieves the unique identifier of this attribute modifier.
   *
   * @return The attribute modifier's unique identifier.
   */
  UUID getUniqueId();

  /**
   * Retrieves the name of this attribute modifier.
   *
   * @return The attribute modifier's name.
   */
  String getName();

  /**
   * Retrieves the value of this attribute modifier.
   *
   * @return The attribute modifier's value.
   */
  float getAmount();

  /**
   * Retrieves the operation of this attribute modifier.
   *
   * @return The attribute modifier's operation.
   */
  AttributeOperation getOperation();

  /**
   * A factory for {@link AttributeModifier}.
   */
  @AssistedFactory(AttributeModifier.class)
  interface Factory {

    /**
     * Creates a new {@link AttributeModifier} with the given {@code name}, {@code amount} and the
     * {@code operation}.
     *
     * @param name      The name of this attribute modifier.
     * @param amount    The amount of this attribute modifier.
     * @param operation The operation to apply this attribute modifier with.
     * @return A created attribute modifier.
     */
    AttributeModifier create(String name, float amount, AttributeOperation operation);

    /**
     * Creates a new {@link AttributeModifier} with the given {@code uniqueId}, {@code name}, {@code
     * amount} and the {@code operation}.
     *
     * @param uniqueId  The unique identifier of this attribute modifier.
     * @param name      The name of this attribute modifier.
     * @param amount    The amount of this attribute modifier.
     * @param operation The operation to apply this attribute modifier with.
     * @return A created attribute modifier.
     */
    AttributeModifier create(UUID uniqueId, String name, float amount,
        AttributeOperation operation);

  }

}
