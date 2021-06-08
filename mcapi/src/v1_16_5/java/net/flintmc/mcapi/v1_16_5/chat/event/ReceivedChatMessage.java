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

package net.flintmc.mcapi.v1_16_5.chat.event;

import net.flintmc.mcapi.chat.ChatLocation;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.minecraft.util.text.ITextComponent;
import java.util.UUID;

public class ReceivedChatMessage {

  private final ITextComponent minecraftComponent;
  private final ChatComponent component;
  private final ChatLocation location;
  private final UUID senderId;

  public ReceivedChatMessage(
      ITextComponent minecraftComponent,
      ChatComponent component,
      ChatLocation location,
      UUID senderId) {
    this.minecraftComponent = minecraftComponent;
    this.component = component;
    this.location = location;
    this.senderId = senderId;
  }

  public ITextComponent getMinecraftComponent() {
    return this.minecraftComponent;
  }

  public ChatComponent getComponent() {
    return this.component;
  }

  public ChatLocation getLocation() {
    return this.location;
  }

  public UUID getSenderId() {
    return this.senderId;
  }
}
