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

import net.flintmc.mcapi.server.buffer.PacketBuffer;

/**
 * Represents a listener that listens to payload channels.
 */
public interface PayloadChannelListener {

  /**
   * Listens to a payload channel that the {@link PayloadChannel} annotation has.
   *
   * @param buffer The payload of the heard channel.
   */
  void listen(PacketBuffer buffer);

  /**
   * A factory class for {@link PayloadChannelListener}.
   */
  interface Factory {

    /**
     * Creates a new {@link PayloadChannel}.
     *
     * @return A created payload channel.
     */
    PayloadChannelListener create();
  }
}
