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

package net.flintmc.mcapi.v1_16_4.server;

import com.google.common.base.Preconditions;
import java.net.InetSocketAddress;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.ServerAddress;

@Implement(value = ServerAddress.class, version = "1.16.4")
public class VersionedServerAddress implements ServerAddress {

  private final String ip;
  private final int port;

  @AssistedInject
  public VersionedServerAddress(@Assisted("ip") String ip, @Assisted("port") int port) {
    this.ip = ip;
    this.port = port;
  }

  @AssistedInject
  public VersionedServerAddress(@Assisted("rawAddress") String rawAddress) {
    net.minecraft.client.multiplayer.ServerAddress address =
        net.minecraft.client.multiplayer.ServerAddress.fromString(rawAddress);
    Preconditions.checkNotNull(address, "address");

    this.ip = address.getIP();
    this.port = address.getPort();
  }

  @AssistedInject
  public VersionedServerAddress(@Assisted("socketAddress") InetSocketAddress address) {
    this(address.getAddress().getHostName(), address.getPort());
  }

  /** {@inheritDoc} */
  @Override
  public String getIP() {
    return this.ip;
  }

  /** {@inheritDoc} */
  @Override
  public int getPort() {
    return this.port;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return this.ip + ":" + this.port;
  }
}
