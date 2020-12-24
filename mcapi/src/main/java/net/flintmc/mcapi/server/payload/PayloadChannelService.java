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

package net.flintmc.mcapi.server.payload;

import com.google.common.collect.Multimap;
import net.flintmc.mcapi.resources.ResourceLocation;

/**
 * Represents a service that registers or unregisters payload channels.
 */
public interface PayloadChannelService {

  /**
   * Registers a new payload channel listener.
   *
   * @param namespace              The namespace of the payload channel.
   * @param path                   The path of the payload channel.
   * @param payloadChannelListener The listener for the payload channel.
   */
  void registerPayloadChannel(
      String namespace, String path, PayloadChannelListener payloadChannelListener);

  /**
   * Registers a new payload channel listener.
   *
   * @param resourceLocation       The channel identifier for the payload channel.
   * @param payloadChannelListener The payload channel listener.
   */
  void registerPayloadChannel(
      ResourceLocation resourceLocation, PayloadChannelListener payloadChannelListener);

  /**
   * Unregister all payload channel listeners bound to the given resource location.
   *
   * @param resourceLocation The channel identifier of the payload channel listeners to be removed.
   */
  void unregisterPayloadChannels(ResourceLocation resourceLocation);

  /**
   * Unregister the given payload channel listener.
   *
   * @param payloadChannelListener The payload channel listener to be removed.
   */
  void unregisterPayloadChannel(PayloadChannelListener payloadChannelListener);

  /**
   * Unregister the the payload channel.
   *
   * @param resourceLocation       The channel identifier of the payload channel.
   * @param payloadChannelListener The payload channel listener to be removed.
   */
  void unregisterPayloadChannel(
      ResourceLocation resourceLocation, PayloadChannelListener payloadChannelListener);

  /**
   * Whether the payload channel should be listened to.
   *
   * @param channelIdentifier The identifier of the payload channel.
   * @param buffer            The non-null Minecraft packet buffer.
   * @return {@code true} if the payload channel should be listened to, otherwise {@code false}.
   */
  boolean shouldListen(String channelIdentifier, Object buffer);

  /**
   * Retrieves a key-value system with all registered payload channels.
   *
   * @return A key-value system with all registered payload channels.
   */
  Multimap<ResourceLocation, PayloadChannelListener> getPayloadChannels();
}
