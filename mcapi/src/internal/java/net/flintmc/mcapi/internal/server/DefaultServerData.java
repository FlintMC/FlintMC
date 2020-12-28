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

package net.flintmc.mcapi.internal.server;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.ServerData;
import net.flintmc.mcapi.server.status.ServerStatus;
import net.flintmc.mcapi.server.status.ServerStatusResolver;

import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;

/**
 * {@inheritDoc}
 */
@Implement(ServerData.class)
public class DefaultServerData implements ServerData {

  private final String name;
  private final ServerAddress address;
  private final ResourceMode resourceMode;

  private final ServerStatusResolver serverStatusResolver;

  @AssistedInject
  private DefaultServerData(
          @Assisted String name,
          @Assisted ServerAddress address,
          @Assisted ResourceMode resourceMode,
          ServerStatusResolver serverStatusResolver) {
    this.name = name;
    this.address = address;
    this.resourceMode = resourceMode;
    this.serverStatusResolver = serverStatusResolver;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<ServerStatus> loadStatus() throws UnknownHostException {
    return this.serverStatusResolver.resolveStatus(this.address);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ServerAddress getServerAddress() {
    return this.address;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceMode getResourceMode() {
    return this.resourceMode;
  }
}
