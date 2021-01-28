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

package net.flintmc.mcapi.v1_16_5.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.event.MinecraftInitializeEvent;
import net.flintmc.transform.hook.Hook;
import net.minecraft.client.GameConfiguration;

@Singleton
public class VersionedMinecraftInitializeEventInjector {

  private final EventBus eventBus;

  private final MinecraftInitializeEvent event;

  @Inject
  public VersionedMinecraftInitializeEventInjector(EventBus eventBus) {
    this.eventBus = eventBus;
    this.event = new MinecraftInitializeEvent() {
    };
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "<init>",
      parameters = {@Type(reference = GameConfiguration.class)},
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      version = "1.16.5")
  public void minecraftInitialize(Hook.ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.event, executionTime);
  }
}
