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

package net.flintmc.mcapi.v1_15_2.server.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.server.event.ServerConnectProgressEvent;
import net.flintmc.mcapi.server.event.ServerConnectProgressEvent.Stage;
import net.flintmc.mcapi.v1_15_2.server.event.shadow.ClientLoginNetHandlerShadow;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

@Singleton
public class ServerConnectProgressEventInjector {

  private static final Map<String, Stage> STAGES = new HashMap<>();

  static {
    STAGES.put("connect.authorizing", Stage.AUTHORIZING);
    STAGES.put("connect.encrypting", Stage.ENCRYPTING);
    STAGES.put("connect.joining", Stage.JOINING);
    STAGES.put("connect.negotiating", Stage.NEGOTIATING);
  }

  private final EventBus eventBus;
  private final MinecraftComponentMapper componentMapper;
  private final ServerConnectProgressEvent.Factory eventFactory;

  @Inject
  public ServerConnectProgressEventInjector(
      EventBus eventBus,
      MinecraftComponentMapper componentMapper,
      ServerConnectProgressEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.componentMapper = componentMapper;
    this.eventFactory = eventFactory;
  }

  @Hook(
      executionTime = ExecutionTime.AFTER,
      className = "net.minecraft.client.network.login.ClientLoginNetHandler",
      methodName = "<init>",
      parameters = {
          @Type(typeName = "net.minecraft.network.NetworkManager"),
          @Type(typeName = "net.minecraft.client.Minecraft"),
          @Type(typeName = "net.minecraft.client.gui.screen.Screen"),
          @Type(typeName = "java.util.function.Consumer")
      })
  public void overrideLoginHandler(
      @Named("instance") Object instance, @Named("args") Object[] args) {
    ClientLoginNetHandlerShadow shadow = (ClientLoginNetHandlerShadow) instance;
    Consumer<ITextComponent> originalConsumer = shadow.getStatusMessageConsumer();

    shadow.setStatusMessageConsumer(statusMessage -> {
      Stage stage = this.getStage(statusMessage);
      ChatComponent flintMessage = this.componentMapper.fromMinecraft(statusMessage);

      Event event = this.eventFactory.create(flintMessage, stage);
      this.eventBus.fireEvent(event, Phase.PRE);

      if (originalConsumer != null) {
        originalConsumer.accept(statusMessage);
      }
    });
  }

  private Stage getStage(ITextComponent component) {
    if (!(component instanceof TranslationTextComponent)) {
      return Stage.UNKNOWN;
    }

    return STAGES.getOrDefault(((TranslationTextComponent) component).getKey(), Stage.UNKNOWN);
  }
}
