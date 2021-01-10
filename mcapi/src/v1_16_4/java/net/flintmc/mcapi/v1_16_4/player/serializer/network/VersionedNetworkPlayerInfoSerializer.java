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

package net.flintmc.mcapi.v1_16_4.player.serializer.network;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.mcapi.player.network.NetworkPlayerInfo;
import net.flintmc.mcapi.player.serializer.network.NetworkPlayerInfoSerializer;

/**
 * 1.16.4 implementation of {@link NetworkPlayerInfoSerializer}
 */
@Singleton
@Implement(value = NetworkPlayerInfoSerializer.class, version = "1.16.4")
public class VersionedNetworkPlayerInfoSerializer
    implements NetworkPlayerInfoSerializer<net.minecraft.client.network.play.NetworkPlayerInfo> {

  /**
   * Deserializes the Mojang {@link net.minecraft.client.network.play.NetworkPlayerInfo} to the
   * Flint {@link NetworkPlayerInfo}
   *
   * @param value The network player info being deserialized
   * @return A deserialized {@link NetworkPlayerInfo}
   */
  @Override
  public NetworkPlayerInfo deserialize(net.minecraft.client.network.play.NetworkPlayerInfo value) {
    return InjectionHolder.getInjectedInstance(NetworkPlayerInfo.class);
  }

  /**
   * Serializes the Flint {@link NetworkPlayerInfo} to the {@link net.minecraft.client.network.play.NetworkPlayerInfo}
   *
   * @param value The network player info being serialized
   * @return A serialized {@link net.minecraft.client.network.play.NetworkPlayerInfo}
   */
  @Override
  @Deprecated
  public net.minecraft.client.network.play.NetworkPlayerInfo serialize(NetworkPlayerInfo value) {
    throw new UnsupportedOperationException("The method is unsupported.");
  }
}
