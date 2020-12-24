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

package net.flintmc.mcapi.internal.server.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.event.ServerKickEvent;

import javax.annotation.Nullable;

/** {@inheritDoc} */
@Implement(ServerKickEvent.class)
public class DefaultServerKickEvent extends DefaultServerAddressEvent implements ServerKickEvent {

  private final ChatComponent reason;

  @AssistedInject
  public DefaultServerKickEvent(
          @Assisted("address") @Nullable ServerAddress address, @Assisted("reason") ChatComponent reason) {
    super(address);
    this.reason = reason;
  }

  /** {@inheritDoc} */
  @Override
  public Direction getDirection() {
    return Direction.RECEIVE;
  }

  /** {@inheritDoc} */
  @Override
  public ChatComponent getReason() {
    return this.reason;
  }
}
