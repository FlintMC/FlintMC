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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.server.buffer.PacketBuffer;

/**
 * Fired when a custom payload message is received from the server.
 */
public interface RetrievePayloadEvent extends PayloadEvent {

  /**
   * A factory class for {@link RetrievePayloadEvent}.
   */
  @AssistedFactory(RetrievePayloadEvent.class)
  interface Factory {

    /**
     * Creates a new {@link RetrievePayloadEvent} with the given parameters.
     *
     * @param identifier The channel identifier where the message should be received.
     * @param buffer     The buffer that contains the payload.
     * @return A created retrieve payload event.
     */
    RetrievePayloadEvent create(
        @Assisted ResourceLocation identifier, @Assisted PacketBuffer buffer);
  }
}
