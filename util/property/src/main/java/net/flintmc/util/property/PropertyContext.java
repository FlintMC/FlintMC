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

package net.flintmc.util.property;

import java.util.Map;

/**
 * Represents a registry of highly generic property.
 *
 * @param <T_PropertyContextAware> reference to the associated {@link PropertyContextAware}. Just
 *     used for generic locking
 * @see Property
 * @see PropertyContextAware
 */
public interface PropertyContext<
    T_PropertyContextAware extends PropertyContextAware<T_PropertyContextAware>> {

  /**
   * @param property the property to modify on this instance
   * @param propertyValue the value to set the property to
   * @param <T_PropertyValue> @see {@link Property< T_PropertyValue >}
   * @param <T_PropertyMeta> @see {@link Property<T_PropertyMeta>}
   * @return this
   */
  <T_PropertyValue, T_PropertyMeta> T_PropertyContextAware setPropertyValue(
      Property<T_PropertyValue, T_PropertyMeta> property, T_PropertyValue propertyValue);

  /**
   * @param property the property to modify on this instance
   * @param propertyMode the mode to set the property to
   * @param <T_PropertyValue> @see {@link Property<T_PropertyValue>}
   * @param <T_PropertyMeta> @see {@link Property<T_PropertyMeta>}
   * @return this
   */
  <T_PropertyValue, T_PropertyMeta> T_PropertyContextAware setPropertyMeta(
      Property<T_PropertyValue, T_PropertyMeta> property, T_PropertyMeta propertyMode);

  /**
   * @param property the property to get the value from
   * @param <T_PropertyValue> @see {@link Property<T_PropertyValue>}
   * @param <T_PropertyMeta> @see {@link Property<T_PropertyMeta>}
   * @return the current value of the given property
   */
  <T_PropertyValue, T_PropertyMeta> T_PropertyValue getPropertyValue(
      Property<T_PropertyValue, T_PropertyMeta> property);

  /**
   * @param property the property to get the mode from
   * @param <T_PropertyValue> @see {@link Property<T_PropertyValue>}
   * @param <T_PropertyMeta> @see {@link Property<T_PropertyMeta>}
   * @return the current mode of the given property
   */
  <T_PropertyValue, T_PropertyMeta> T_PropertyMeta getPropertyMeta(
      Property<T_PropertyValue, T_PropertyMeta> property);

  /**
   * @return all defined properties as a map.
   */
  Map<Property<?, ?>, Object> getProperties();

  /** A factory class for {@link PropertyContext}. */
  interface Factory {

    /**
     * Creates an instance of {@link PropertyContext} from the given parameters.
     *
     * @param propertyContextAware     the associated {@link PropertyContextAware}.
     * @param <T_PropertyContextAware> the type of propertyContextAware. Just used for generic
     *                                 locking.
     * @return the created instance
     */
    <T_PropertyContextAware extends PropertyContextAware<T_PropertyContextAware>>
    PropertyContext<T_PropertyContextAware> create(T_PropertyContextAware propertyContextAware);
  }
}
