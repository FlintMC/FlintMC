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

import net.flintmc.framework.inject.assisted.AssistedFactory;

public interface AttributeMap {

  /**
   * Registers a new {@link Attribute}.
   *
   * @param attribute The attribute to be registered.
   * @return This map for chaining.
   */
  AttributeMap register(Attribute attribute);

  /**
   * Retrieves an attribute with the given {@code key}.
   *
   * @param key The key of an attribute.
   * @return An attribute with the key or {@code null}.
   */
  Attribute get(String key);

  /**
   * Retrieves an array of all registered attributes.
   *
   * @return An array of all registered attributes.
   */
  Attribute[] values();

  /**
   * Retrieves an array of all registered attributes to be communicated to the client.
   *
   * @return An array of all registered attributes to be communicated to the client.
   */
  Attribute[] sharedValues();

  /**
   * A factory for the {@link AttributeMap}.
   */
  @AssistedFactory(AttributeMap.class)
  interface Factory {

    /**
     * Creates a new {@link AttributeMap}.
     *
     * @return A created attribute map.
     */
    AttributeMap create();
  }
}
