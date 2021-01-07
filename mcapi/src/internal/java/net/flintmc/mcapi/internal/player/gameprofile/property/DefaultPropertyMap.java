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

package net.flintmc.mcapi.internal.player.gameprofile.property;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.property.Property;
import net.flintmc.mcapi.player.gameprofile.property.PropertyMap;

/**
 * An implementation of {@link PropertyMap}
 */
@Implement(PropertyMap.class)
public class DefaultPropertyMap extends ForwardingMultimap<String, Property>
    implements PropertyMap {

  private final Multimap<String, Property> properties;

  @AssistedInject
  private DefaultPropertyMap() {
    this.properties = LinkedHashMultimap.create();
  }

  @AssistedInject
  private DefaultPropertyMap(@Assisted("properties") Multimap<String, Property> properties) {
    this.properties = properties;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Multimap<String, Property> delegate() {
    return properties;
  }
}
