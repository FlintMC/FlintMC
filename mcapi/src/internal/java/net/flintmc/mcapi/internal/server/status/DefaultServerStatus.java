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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.status.ServerFavicon;
import net.flintmc.mcapi.server.status.ServerPlayers;
import net.flintmc.mcapi.server.status.ServerStatus;
import net.flintmc.mcapi.server.status.ServerVersion;

@Implement(ServerStatus.class)
public class DefaultServerStatus implements ServerStatus {

  private final ServerAddress sourceAddress;
  private final ServerVersion version;
  private final ServerPlayers players;
  private final ChatComponent description;
  private final ServerFavicon favicon;
  private final long ping;
  private final long timestamp;

  @AssistedInject
  public DefaultServerStatus(
      @Assisted("sourceAddress") ServerAddress sourceAddress,
      @Assisted("version") ServerVersion version,
      @Assisted("players") ServerPlayers players,
      @Assisted("description") ChatComponent description,
      @Assisted("favicon") ServerFavicon favicon,
      @Assisted("ping") long ping) {
    this.sourceAddress = sourceAddress;
    this.version = version;
    this.players = players;
    this.description = description;
    this.favicon = favicon;
    this.ping = ping;
    this.timestamp = System.currentTimeMillis();
  }

  /** {@inheritDoc} */
  @Override
  public ServerAddress getSourceAddress() {
    return this.sourceAddress;
  }

  /** {@inheritDoc} */
  @Override
  public ServerVersion getVersion() {
    return this.version;
  }

  /** {@inheritDoc} */
  @Override
  public ServerPlayers getPlayers() {
    return this.players;
  }

  /** {@inheritDoc} */
  @Override
  public ChatComponent getDescription() {
    return this.description;
  }

  /** {@inheritDoc} */
  @Override
  public ServerFavicon getFavicon() {
    return this.favicon;
  }

  /** {@inheritDoc} */
  @Override
  public long getPing() {
    return this.ping;
  }

  /** {@inheritDoc} */
  @Override
  public long getTimestamp() {
    return this.timestamp;
  }
}
