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

import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.server.buffer.PacketBuffer;
import net.flintmc.mcapi.server.payload.PayloadEvent;

public class DefaultPayloadEvent implements PayloadEvent {

  private final ResourceLocation identifier;
  private final PacketBuffer buffer;
  private boolean cancelled;

  public DefaultPayloadEvent(ResourceLocation identifier, PacketBuffer buffer) {
    this.identifier = identifier;
    this.buffer = buffer;
    this.cancelled = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getIdentifier() {
    return this.identifier;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PacketBuffer getBuffer() {
    return this.buffer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
