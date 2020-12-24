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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents an attribute for entities.
 */
public interface Attribute {

  /**
   * Retrieves the unique identifier of this attribute.
   *
   * @return The attribute's unique identifier.
   */
  String getKey();

  /**
   * Retrieves the attribute default value that should be applied.
   *
   * @return The default attribute value.
   */
  float getDefaultValue();

  /**
   * Retrieves the maximum value applicable to an entity for this attribute.
   *
   * @return The maximum value of this attribute.
   */
  float getMaximumValue();

  /**
   * Whether the attribute is shared to the client.
   *
   * @return {@code true} if the attribtue is shared to the client, otherwise {@code false}.
   */
  boolean isShared();

  @AssistedFactory(Attribute.class)
  interface Factory {

    /**
     * Creates a new {@link Attribute} wit the {@code key}, {@code defaultValue} and the {@code
     * maximumValue}.
     *
     * @param key          The registry key for this attribute.
     * @param defaultValue The default value for this attribute.
     * @param maximumValue The maximum value for this attribute.
     * @return A created attribute.
     */
    Attribute create(
        @Assisted("key") String key,
        @Assisted("defaultValue") float defaultValue,
        @Assisted("maximumVale") float maximumValue);

    /**
     * Creates a new {@link Attribute} with the {@code key}, {@code defaultValue}, {@code
     * maximumValue} and {@code shared} if this attribute is to be used by the client.
     *
     * @param key          The registry key for this attribute.
     * @param defaultValue The default value for this attribute.
     * @param maximumValue The maximum value for this attribute.
     * @param shared       {@code true} if this attribute is to be used by the client, otherwise
     *                     {@code false}.
     * @return A created attribute.
     */
    Attribute create(
        @Assisted("key") String key,
        @Assisted("defaultValue") float defaultValue,
        @Assisted("maximumValue") float maximumValue,
        @Assisted("shared") boolean shared);
  }
}
