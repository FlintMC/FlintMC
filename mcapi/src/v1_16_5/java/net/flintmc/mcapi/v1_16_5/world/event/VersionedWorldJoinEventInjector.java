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

package net.flintmc.mcapi.v1_16_5.world.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mojang.datafixers.util.Function4;
import java.util.function.Function;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.server.event.ServerConnectEvent;
import net.flintmc.mcapi.world.event.WorldJoinEvent;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;
import net.minecraft.util.registry.DynamicRegistries;

@Singleton
public class VersionedWorldJoinEventInjector {

  private final EventBus eventBus;
  private final WorldJoinEvent.Factory eventFactory;

  @Inject
  private VersionedWorldJoinEventInjector(EventBus eventBus, WorldJoinEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
  }

  @Subscribe(phase = Phase.ANY, version = "1.16.5")
  public void fireSingleplayerWorldJoinEvent(ServerConnectEvent event, Phase phase) {
    WorldJoinEvent joinEvent = this.eventFactory
        .create(event.getAddress().toString(), WorldJoinEvent.Type.MULTIPLAYER);
    this.eventBus.fireEvent(joinEvent, phase);
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "loadWorld",
      parameters = {
          @Type(reference = String.class),
          @Type(reference = DynamicRegistries.Impl.class),
          @Type(reference = Function.class),
          @Type(reference = Function4.class),
          @Type(reference = boolean.class),
          @Type(typeName = "net.minecraft.client.Minecraft$WorldSelectionType")
      },
      executionTime = {ExecutionTime.BEFORE, ExecutionTime.AFTER},
      version = "1.16.5"
  )
  public void launchIntegratedServer(ExecutionTime executionTime, @Named("args") Object[] args) {
    WorldJoinEvent event = this.eventFactory
        .create((String) args[0], WorldJoinEvent.Type.SINGLEPLAYER);
    this.eventBus.fireEvent(event, executionTime);
  }
}
