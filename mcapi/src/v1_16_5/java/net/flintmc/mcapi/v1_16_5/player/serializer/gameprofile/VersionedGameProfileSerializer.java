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
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.serializer.gameprofile.GameProfileSerializer;
import net.flintmc.mcapi.player.serializer.gameprofile.PropertyMapSerializer;

/**
 * 1.16.5 implementation of {@link GameProfileSerializer}
 */
@Singleton
@Implement(value = GameProfileSerializer.class, version = "1.16.5")
public class VersionedGameProfileSerializer
    implements GameProfileSerializer<com.mojang.authlib.GameProfile> {

  private final GameProfile.Builder profileBuilder;
  private final PropertyMapSerializer<PropertyMap> propertyMapSerializer;

  @Inject
  public VersionedGameProfileSerializer(
      GameProfile.Builder profileBuilder, PropertyMapSerializer propertyMapSerializer) {
    this.profileBuilder = profileBuilder;
    this.propertyMapSerializer = propertyMapSerializer;
  }

  /**
   * Deserializes the Mojang {@link com.mojang.authlib.GameProfile} to the Flint {@link
   * GameProfile}
   *
   * @param profile The game profile to deserialize
   * @return A deserialized {@link GameProfile}
   */
  @Override
  public GameProfile deserialize(com.mojang.authlib.GameProfile profile) {
    return this.profileBuilder
        .setName(profile.getName())
        .setUniqueId(profile.getId())
        .setProperties(this.propertyMapSerializer.deserialize(profile.getProperties()))
        .build();
  }

  /**
   * Serializes the Flint {@link GameProfile} to the Mojang {@link com.mojang.authlib.GameProfile}.
   *
   * @param profile The profile to serialize
   * @return A serialized game profile
   */
  @Override
  public com.mojang.authlib.GameProfile serialize(GameProfile profile) {
    com.mojang.authlib.GameProfile gameProfile =
        new com.mojang.authlib.GameProfile(profile.getUniqueId(), profile.getName());

    PropertyMap properties = this.propertyMapSerializer.serialize(profile.getProperties());

    for (Map.Entry<String, Property> entry : properties.entries()) {
      gameProfile.getProperties().put(entry.getKey(), entry.getValue());
    }
    return gameProfile;
  }
}
