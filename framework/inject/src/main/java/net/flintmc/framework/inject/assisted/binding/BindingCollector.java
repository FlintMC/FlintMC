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

package net.flintmc.framework.inject.assisted.binding;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.inject.ConfigurationException;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Message;
import java.util.Collections;
import java.util.Map;

/** A collector for collecting factory bindings. */
public class BindingCollector {

  private final Map<Key<?>, TypeLiteral<?>> bindings = Maps.newHashMap();

  /**
   * Adds a {@code target} to the given {@code key}.
   *
   * @param key The key with which teh specified {@code target} is to be associated.
   * @param target The target to be associated with the specified {@code key}.
   * @return The collector, for chaining.
   */
  public BindingCollector addBinding(Key<?> key, TypeLiteral<?> target) {
    if (bindings.containsKey(key)) {
      throw new ConfigurationException(
          ImmutableSet.of(new Message("Only one implementation can be specified for " + key)));
    }

    bindings.put(key, target);

    return this;
  }

  /**
   * Retrieves a key-value system with all bindings.
   *
   * @return A key-value ystem with all binding.
   */
  public Map<Key<?>, TypeLiteral<?>> getBindings() {
    return Collections.unmodifiableMap(bindings);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return bindings.hashCode();
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object object) {
    return (object instanceof BindingCollector)
        && bindings.equals(((BindingCollector) object).bindings);
  }
}
