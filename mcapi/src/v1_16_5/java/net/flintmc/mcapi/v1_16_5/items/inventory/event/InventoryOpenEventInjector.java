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

package net.flintmc.mcapi.v1_16_5.items.inventory.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.items.inventory.InventoryController;
import net.flintmc.mcapi.items.inventory.event.InventoryOpenEvent;
import net.flintmc.transform.hook.Hook;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;

@Singleton
public class InventoryOpenEventInjector {

  private final EventBus eventBus;
  private final InventoryController controller;
  private final InventoryOpenEvent.Factory eventFactory;

  @Inject
  public InventoryOpenEventInjector(EventBus eventBus, InventoryController controller,
      InventoryOpenEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.controller = controller;
    this.eventFactory = eventFactory;
  }

  @Hook(
      className = "net.minecraft.client.gui.ScreenManager$IScreenFactory",
      methodName = "createScreen",
      parameters = {
          @Type(typeName = "net.minecraft.util.text.ITextComponent"),
          @Type(typeName = "net.minecraft.inventory.container.ContainerType"),
          @Type(typeName = "net.minecraft.client.Minecraft"),
          @Type(reference = int.class)
      },
      version = "1.16.5")
  public void createScreen() {
    InventoryOpenEvent event = this.eventFactory.create(this.controller.getOpenInventory());
    this.eventBus.fireEvent(event, Subscribe.Phase.POST);
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "displayGuiScreen",
      parameters = @Type(typeName = "net.minecraft.client.gui.screen.Screen"),
      executionTime = Hook.ExecutionTime.AFTER,
      version = "1.16.5")
  public void displayGuiScreen(@Named("args") Object[] args) {
    if (!(args[0] instanceof InventoryScreen)) {
      return;
    }

    InventoryOpenEvent event = this.eventFactory.create(this.controller.getPlayerInventory());
    this.eventBus.fireEvent(event, Subscribe.Phase.POST);
  }

}
