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

package net.flintmc.mcapi.internal.server.status;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.status.ServerFavicon;
import net.flintmc.mcapi.server.status.ServerStatus;
import net.flintmc.mcapi.server.status.ServerStatusResolver;
import net.flintmc.mcapi.server.status.pending.PendingStatusRequest;

import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;

@Singleton
@Implement(ServerStatusResolver.class)
public class DefaultServerStatusResolver implements ServerStatusResolver {

  private final PendingStatusRequest.Factory statusRequestFactory;
  private final ServerFavicon defaultFavicon;

  @Inject
  public DefaultServerStatusResolver(
      ResourceLocationProvider resourceLocationProvider,
      PendingStatusRequest.Factory statusRequestFactory,
      ServerFavicon.Factory faviconFactory) {
    this.statusRequestFactory = statusRequestFactory;
    this.defaultFavicon =
        faviconFactory.createDefault(
            resourceLocationProvider.get("textures/misc/unknown_server.png"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<ServerStatus> resolveStatus(ServerAddress address)
      throws UnknownHostException {
    PendingStatusRequest request = this.statusRequestFactory.create(address, this.defaultFavicon);
    request.start();
    return request.getFuture();
  }
}
