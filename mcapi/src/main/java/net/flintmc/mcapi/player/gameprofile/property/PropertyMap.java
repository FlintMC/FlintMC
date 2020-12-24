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

package net.flintmc.mcapi.player.gameprofile.property;

import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents the properties of a game profile.
 */
public interface PropertyMap extends Multimap<String, Property> {

  /**
   * A json serializer and deserializer of {@link PropertyMap}
   */
  interface Serializer extends JsonSerializer<PropertyMap>, JsonDeserializer<PropertyMap> {

  }

  /**
   * A factory class for the {@link PropertyMap}.
   */
  @AssistedFactory(PropertyMap.class)
  interface Factory {

    /**
     * Creates a new {@link PropertyMap}.
     *
     * @return A created property map.
     */
    PropertyMap create();

    /**
     * Creates a new {@link PropertyMap} with the given {@link Multimap}.
     *
     * @param properties The multimap for the property map.
     * @return A created property map.
     */
    PropertyMap create(@Assisted("properties") Multimap<String, Property> properties);
  }
}
