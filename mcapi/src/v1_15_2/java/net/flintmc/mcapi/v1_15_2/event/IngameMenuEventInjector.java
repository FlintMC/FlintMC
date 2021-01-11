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

package net.flintmc.mcapi.v1_15_2.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.event.IngameMenuCloseEvent;
import net.flintmc.mcapi.event.IngameMenuOpenEvent;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.HookResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.IngameMenuScreen;

@Singleton
public class IngameMenuEventInjector {

  private final EventBus eventBus;
  private final IngameMenuOpenEvent openEvent;
  private final IngameMenuCloseEvent closeEvent;

  @Inject
  private IngameMenuEventInjector(
      EventBus eventBus,
      IngameMenuOpenEvent.Factory openEventFactory,
      IngameMenuCloseEvent.Factory closeEventFactory) {
    this.eventBus = eventBus;
    this.openEvent = openEventFactory.create();
    this.closeEvent = closeEventFactory.create();
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "displayInGameMenu",
      parameters = @Type(reference = boolean.class),
      executionTime = Hook.ExecutionTime.BEFORE,
      version = "1.15.2")
  public HookResult displayInGameMenu(Hook.ExecutionTime executionTime) {
    if (Minecraft.getInstance().currentScreen != null) {
      return HookResult.CONTINUE;
    }

    this.openEvent.setCancelled(false);
    return this.eventBus.fireEvent(this.openEvent, executionTime).isCancelled()
        ? HookResult.BREAK
        : HookResult.CONTINUE;
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "displayGuiScreen",
      parameters = @Type(typeName = "net.minecraft.client.gui.screen.Screen"),
      executionTime = Hook.ExecutionTime.BEFORE,
      version = "1.15.2")
  public void displayGuiScreen(@Named("args") Object[] args, Hook.ExecutionTime executionTime) {
    Object screen = args[0];
    if (screen != null) {
      return;
    }
    if (!(Minecraft.getInstance().currentScreen instanceof IngameMenuScreen)) {
      return;
    }

    this.eventBus.fireEvent(this.closeEvent, executionTime);
  }
}
