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

package net.flintmc.util.internal.attribute;

import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.attribute.Attribute;
import net.flintmc.util.attribute.AttributeMap;

@Implement(AttributeMap.class)
public class DefaultAttributeMap implements AttributeMap {

  private final Map<String, Attribute> attributes;

  @AssistedInject
  public DefaultAttributeMap() {
    this.attributes = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AttributeMap register(Attribute attribute) {
    this.attributes.put(attribute.getKey(), attribute);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Attribute get(String key) {
    return this.attributes.get(key);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Attribute[] values() {
    return this.attributes.values().toArray(new Attribute[0]);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Attribute[] sharedValues() {
    return this.attributes.values().stream().filter(Attribute::isShared).toArray(Attribute[]::new);
  }
}
