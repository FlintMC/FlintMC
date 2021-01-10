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

package net.flintmc.mcapi.internal.server.payload;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map.Entry;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.flintmc.mcapi.server.buffer.PacketBuffer;
import net.flintmc.mcapi.server.buffer.PacketBuffer.Factory;
import net.flintmc.mcapi.server.payload.PayloadChannelListener;
import net.flintmc.mcapi.server.payload.PayloadChannelService;

@Singleton
@Implement(PayloadChannelService.class)
public class DefaultPayloadChannelService implements PayloadChannelService {

  private final Multimap<ResourceLocation, PayloadChannelListener> payloadChannels;
  private final PacketBuffer.Factory packetBufferFactory;
  private final ResourceLocationProvider resourceLocationProvider;

  @Inject
  private DefaultPayloadChannelService(
      Factory packetBufferFactory, ResourceLocationProvider resourceLocationProvider) {
    this.packetBufferFactory = packetBufferFactory;
    this.resourceLocationProvider = resourceLocationProvider;
    this.payloadChannels = HashMultimap.create();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerPayloadChannel(
      String namespace, String path, PayloadChannelListener payloadChannelListener) {
    this.registerPayloadChannel(
        this.resourceLocationProvider.get(namespace, path), payloadChannelListener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerPayloadChannel(
      ResourceLocation resourceLocation, PayloadChannelListener payloadChannelListener) {
    this.payloadChannels.put(resourceLocation, payloadChannelListener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregisterPayloadChannels(ResourceLocation resourceLocation) {
    this.payloadChannels.removeAll(resourceLocation);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregisterPayloadChannel(PayloadChannelListener payloadChannelListener) {
    this.payloadChannels.values().remove(payloadChannelListener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregisterPayloadChannel(
      ResourceLocation resourceLocation, PayloadChannelListener payloadChannelListener) {
    this.payloadChannels.remove(resourceLocation, payloadChannelListener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean shouldListen(String channelIdentifier, Object buffer) {
    boolean shouldListen = false;

    for (Entry<ResourceLocation, PayloadChannelListener> entry : this.payloadChannels.entries()) {
      if (entry.getKey().toString().equals(channelIdentifier)) {
        entry.getValue().listen(this.packetBufferFactory.create(buffer));
        shouldListen = true;
      }
    }

    return shouldListen;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Multimap<ResourceLocation, PayloadChannelListener> getPayloadChannels() {
    return this.payloadChannels;
  }
}
