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

import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.ServerData;
import net.flintmc.mcapi.server.status.ServerStatus;
import net.flintmc.mcapi.server.status.ServerStatusResolver;

@Implement(ServerData.class)
public class DefaultServerData implements ServerData {

  private final ServerStatusResolver serverStatusResolver;

  private final String name;
  private final Supplier<ServerAddress> addressProvider;
  private final ResourceMode resourceMode;

  @AssistedInject
  private DefaultServerData(
      ServerStatusResolver serverStatusResolver,
      @Assisted String name,
      @Assisted("addressProvider") Supplier<ServerAddress> addressProvider,
      @Assisted ResourceMode resourceMode) {
    this.serverStatusResolver = serverStatusResolver;

    this.name = name;
    this.addressProvider = addressProvider;
    this.resourceMode = resourceMode;
  }

  @AssistedInject
  private DefaultServerData(
      ServerStatusResolver serverStatusResolver,
      @Assisted String name,
      @Assisted("address") ServerAddress address,
      @Assisted ResourceMode resourceMode) {
    this(serverStatusResolver, name, () -> address, resourceMode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<ServerStatus> loadStatus() throws UnknownHostException {
    return this.serverStatusResolver.resolveStatus(this.getServerAddress());
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
    return this.addressProvider.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceMode getResourceMode() {
    return this.resourceMode;
  }
}
