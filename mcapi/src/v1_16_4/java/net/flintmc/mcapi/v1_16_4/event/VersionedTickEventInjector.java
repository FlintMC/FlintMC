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

package net.flintmc.mcapi.v1_16_4.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.mcapi.event.TickEvent;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;

@Singleton
public class VersionedTickEventInjector {

  private final EventBus eventBus;
  private final TickEvent generalTickEvent;
  private final TickEvent gameRenderTickEvent;
  private final TickEvent worldRenderTickEvent;

  @Inject
  private VersionedTickEventInjector(EventBus eventBus, TickEvent.Factory factory) {
    this.eventBus = eventBus;
    this.generalTickEvent = factory.create(TickEvent.Type.GENERAL);
    this.gameRenderTickEvent = factory.create(TickEvent.Type.GAME_RENDER);
    this.worldRenderTickEvent = factory.create(TickEvent.Type.WORLD_RENDER);
  }

  @Hook(
      executionTime = {ExecutionTime.BEFORE, ExecutionTime.AFTER},
      className = "net.minecraft.client.Minecraft",
      methodName = "runTick")
  public void handleGeneralTick(ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.generalTickEvent, executionTime);
  }

  @Hook(
      executionTime = {ExecutionTime.BEFORE, ExecutionTime.AFTER},
      className = "net.minecraft.client.renderer.GameRenderer",
      methodName = "tick")
  public void handleGameRenderTick(ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.gameRenderTickEvent, executionTime);
  }

  @Hook(
      executionTime = {ExecutionTime.BEFORE, ExecutionTime.AFTER},
      className = "net.minecraft.client.renderer.WorldRenderer",
      methodName = "tick")
  public void hookWorldRenderTick(ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.worldRenderTickEvent, executionTime);
  }
}
