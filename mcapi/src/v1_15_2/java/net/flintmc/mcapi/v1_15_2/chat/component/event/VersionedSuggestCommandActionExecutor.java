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

package net.flintmc.mcapi.v1_15_2.chat.component.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.mcapi.chat.ChatController;
import net.flintmc.mcapi.chat.component.event.ClickActionExecutor;
import net.flintmc.mcapi.chat.component.event.ClickEvent;
import net.flintmc.mcapi.chat.component.event.ClickEvent.Action;
import net.flintmc.mcapi.chat.component.event.ClickEventAction;

@Singleton
@ClickEventAction(value = Action.SUGGEST_COMMAND, priority = 0)
public class VersionedSuggestCommandActionExecutor implements ClickActionExecutor {

  private final ChatController chatController;

  @Inject
  private VersionedSuggestCommandActionExecutor(ChatController chatController) {
    this.chatController = chatController;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void executeEvent(ClickEvent event) {
    this.chatController.setInputValue(event.getValue(), true);
  }
}
