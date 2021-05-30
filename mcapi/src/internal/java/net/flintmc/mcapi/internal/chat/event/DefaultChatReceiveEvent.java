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

package net.flintmc.mcapi.internal.chat.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.ChatLocation;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.event.ChatReceiveEvent;
import net.flintmc.mcapi.settings.game.MinecraftConfiguration;
import net.flintmc.mcapi.settings.game.settings.ChatVisibility;
import java.util.UUID;

@Implement(ChatReceiveEvent.class)
public class DefaultChatReceiveEvent extends DefaultChatMessageEvent implements ChatReceiveEvent {

  private final MinecraftConfiguration configuration;
  private final ChatLocation location;
  private final UUID senderId;

  private ChatComponent message;
  private boolean cancelled;

  @AssistedInject
  private DefaultChatReceiveEvent(
      MinecraftConfiguration configuration,
      @Assisted("location") ChatLocation location,
      @Assisted("message") ChatComponent message,
      @Assisted("senderId") UUID senderId) {
    super(Type.RECEIVE);
    this.configuration = configuration;
    this.location = location;
    this.message = message;
    this.senderId = senderId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatLocation getLocation() {
    return this.location;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isVisible() {
    ChatVisibility visibility = this.configuration.getChatConfiguration().getChatVisibility();
    switch (visibility) {
      case SYSTEM:
        return this.location == ChatLocation.SYSTEM;
      case FULL:
        return true;
      default:
      case HIDDEN:
        return false;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getSenderId() {
    return this.senderId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getMessage() {
    return this.message;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMessage(ChatComponent message) {
    this.message = message;
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
