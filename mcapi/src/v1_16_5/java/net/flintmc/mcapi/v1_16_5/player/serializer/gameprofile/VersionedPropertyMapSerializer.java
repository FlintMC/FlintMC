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

package net.flintmc.mcapi.v1_16_5.player.serializer.gameprofile;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.property.Property;
import net.flintmc.mcapi.player.gameprofile.property.PropertyMap;
import net.flintmc.mcapi.player.serializer.gameprofile.PropertyMapSerializer;

/**
 * 1.16.5. implementation of {@link PropertyMapSerializer}
 */
@Singleton
@Implement(value = PropertyMapSerializer.class)
public class VersionedPropertyMapSerializer
    implements PropertyMapSerializer<com.mojang.authlib.properties.PropertyMap> {

  private final Property.Factory propertyFactory;
  private final PropertyMap.Factory propertyMapFactory;

  @Inject
  private VersionedPropertyMapSerializer(
      Property.Factory propertyFactory, PropertyMap.Factory propertyMapFactory) {
    this.propertyFactory = propertyFactory;
    this.propertyMapFactory = propertyMapFactory;
  }

  /**
   * Deserializes the Mojang {@link com.mojang.authlib.properties.PropertyMap} to the Flint {@link
   * PropertyMap}
   *
   * @param value The properties being deserialized
   * @return A deserialized {@link PropertyMap}
   */
  @Override
  public PropertyMap deserialize(com.mojang.authlib.properties.PropertyMap value) {
    PropertyMap properties = this.propertyMapFactory.create();

    for (Map.Entry<String, com.mojang.authlib.properties.Property> entry : value.entries()) {
      properties.put(
          entry.getKey(),
          this.propertyFactory.create(
              entry.getValue().getName(),
              entry.getValue().getValue(),
              entry.getValue().getSignature()));
    }
    return properties;
  }

  /**
   * Serializes the Flint {@link com.mojang.authlib.properties.PropertyMap} to the Mojang {@link
   * com.mojang.authlib.properties.PropertyMap}
   *
   * @param value The properties being serialized
   * @return A serialized {@link com.mojang.authlib.properties.PropertyMap}
   */
  @Override
  public com.mojang.authlib.properties.PropertyMap serialize(PropertyMap value) {
    com.mojang.authlib.properties.PropertyMap properties =
        new com.mojang.authlib.properties.PropertyMap();

    for (Map.Entry<String, Property> entry : value.entries()) {
      properties.put(
          entry.getKey(),
          new com.mojang.authlib.properties.Property(
              entry.getValue().getName(),
              entry.getValue().getValue(),
              entry.getValue().getSignature()));
    }

    return properties;
  }
}
