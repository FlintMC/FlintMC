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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.chat.event.ChatInputChangeEvent;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;

@Singleton
public class ChatInputChangeEventInjector {

  private final EventBus eventBus;

  private final ChatInputChangeEvent.Factory eventFactory;

  @Inject
  private ChatInputChangeEventInjector(
      EventBus eventBus, ChatInputChangeEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
  }

  @Hook(
      className = "net.minecraft.client.gui.screen.ChatScreen",
      methodName = "func_212997_a",
      parameters = @Type(reference = String.class),
      executionTime = ExecutionTime.AFTER
  )
  public void onTextChanged(@Named("args") Object[] args) {
    this.eventBus.fireEvent(this.eventFactory.create((String) args[0]), Phase.POST);
  }
}
