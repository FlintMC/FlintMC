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

package net.flintmc.util.property.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.property.Property;
import net.flintmc.util.property.PropertyContext;
import net.flintmc.util.property.PropertyContextAware;

/**
 * {@inheritDoc}
 */
public class DefaultPropertyContext<
    T_PropertyContextAware extends PropertyContextAware<T_PropertyContextAware>>
    implements PropertyContext<T_PropertyContextAware> {

  private final T_PropertyContextAware propertyContextAware;
  private final Map<Property<?, ?>, Object> propertyValues = new HashMap<>();
  private final Map<Property<?, ?>, Object> propertyMeta = new HashMap<>();

  private DefaultPropertyContext(T_PropertyContextAware propertyContextAware) {
    this.propertyContextAware = propertyContextAware;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T_PropertyValue, T_PropertyMeta> T_PropertyContextAware setPropertyValue(
      Property<T_PropertyValue, T_PropertyMeta> property, T_PropertyValue propertyValue) {
    if (!property.validateValue(propertyValue))
      throw new IllegalArgumentException("provided property value is invalid.");
    this.propertyValues.put(property, propertyValue);
    return this.propertyContextAware;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T_PropertyValue, T_PropertyMeta> T_PropertyContextAware setPropertyMeta(
      Property<T_PropertyValue, T_PropertyMeta> property, T_PropertyMeta propertyMeta) {
    if (!property.validateMeta(propertyMeta))
      throw new IllegalArgumentException("provided property meta is invalid.");
    this.propertyMeta.put(property, propertyMeta);
    return this.propertyContextAware;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T_PropertyValue, T_PropertyMeta> T_PropertyValue getPropertyValue(
      Property<T_PropertyValue, T_PropertyMeta> property) {
    return (T_PropertyValue) this.propertyValues.getOrDefault(property, property.getDefaultValue());
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T_PropertyValue, T_PropertyMeta> T_PropertyMeta getPropertyMeta(
      Property<T_PropertyValue, T_PropertyMeta> property) {
    return (T_PropertyMeta) this.propertyMeta.getOrDefault(property, property.getDefaultMeta());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<Property<?, ?>, Object> getProperties() {
    return Collections.unmodifiableMap(this.propertyValues);
  }

  @Implement(PropertyContext.Factory.class)
  public static class Factory implements PropertyContext.Factory {

    @Override
    public <T_PropertyContextAware extends PropertyContextAware<T_PropertyContextAware>>
    PropertyContext<T_PropertyContextAware> create(
        T_PropertyContextAware propertyContextAware) {
      return new DefaultPropertyContext<T_PropertyContextAware>(propertyContextAware);
    }
  }
}
