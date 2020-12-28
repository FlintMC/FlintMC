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

package net.flintmc.util.mojang.internal.profile;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.gameprofile.property.Property;
import net.flintmc.mcapi.player.gameprofile.property.PropertyMap;
import net.flintmc.util.mojang.internal.cache.DataStreamHelper;
import net.flintmc.util.mojang.internal.cache.object.CacheIO;
import net.flintmc.util.mojang.internal.cache.object.CachedObjectIO;

@Singleton
@CacheIO
public class GameProfileIO implements CachedObjectIO<GameProfile> {

  private final GameProfile.Factory profileFactory;
  private final Property.Factory propertyFactory;

  @Inject
  private GameProfileIO(GameProfile.Factory profileFactory, Property.Factory propertyFactory) {
    this.profileFactory = profileFactory;
    this.propertyFactory = propertyFactory;
  }

  @Override
  public Class<GameProfile> getType() {
    return GameProfile.class;
  }

  @Override
  public void write(UUID uniqueId, GameProfile profile, DataOutput output) throws IOException {
    DataStreamHelper.writeString(output, profile.getName());

    PropertyMap properties = profile.getProperties();
    output.writeByte(properties.size());

    for (Property property : properties.values()) {
      DataStreamHelper.writeString(output, property.getName());
      DataStreamHelper.writeString(output, property.getValue());
      output.writeBoolean(property.hasSignature());
      if (property.hasSignature()) {
        DataStreamHelper.writeString(output, property.getSignature());
      }
    }
  }

  @Override
  public GameProfile read(UUID uniqueId, DataInput input) throws IOException {
    GameProfile profile = this.profileFactory.create(uniqueId, DataStreamHelper.readString(input));

    int size = input.readByte();
    for (int i = 0; i < size; i++) {
      String name = DataStreamHelper.readString(input);
      String value = DataStreamHelper.readString(input);
      String signature = input.readBoolean() ? DataStreamHelper.readString(input) : null;
      if (signature != null) {
        profile.getProperties().put(name, this.propertyFactory.create(name, value, signature));
      } else {
        profile.getProperties().put(name, this.propertyFactory.create(name, value));
      }
    }

    return profile;
  }
}
