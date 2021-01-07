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

package net.flintmc.mcapi.v1_16_4.items.inventory.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.mcapi.event.DirectionalEvent.Direction;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.InventoryController;
import net.flintmc.mcapi.items.inventory.event.InventoryCloseEvent;
import net.flintmc.mcapi.server.event.PacketEvent;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.HookResult;

@Singleton
public class InventoryCloseEventInjector {

  private final EventBus eventBus;
  private final InventoryCloseEvent.Factory eventFactory;
  private final InventoryController controller;

  @Inject
  public InventoryCloseEventInjector(
      EventBus eventBus, InventoryCloseEvent.Factory eventFactory, InventoryController controller) {
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
    this.controller = controller;
  }

  @Subscribe(phase = Subscribe.Phase.ANY)
  public void fireServerClose(PacketEvent event, Subscribe.Phase phase) {
    if (event.getDirection() != Direction.RECEIVE
        || !(event.getPacket() instanceof AccessibleSCloseWindowPacket)) {
      return;
    }

    AccessibleSCloseWindowPacket packet = (AccessibleSCloseWindowPacket) event.getPacket();
    Inventory inventory = this.controller.getOpenInventory();
    if (inventory == null || inventory.getWindowId() != packet.getWindowId()) {
      return;
    }

    this.eventBus.fireEvent(this.eventFactory.create(inventory, Direction.RECEIVE), phase);
  }

  @Hook(
      executionTime = Hook.ExecutionTime.BEFORE,
      className = "net.minecraft.client.entity.player.ClientPlayerEntity",
      methodName = "closeScreen",
      version = "1.16.4")
  public HookResult closeScreen() {
    Inventory inventory = this.controller.getOpenInventory();
    if (inventory == null) {
      inventory = this.controller.getPlayerInventory();
    }

    boolean cancelled =
        this.eventBus
            .fireEvent(this.eventFactory.create(inventory, Direction.SEND), Subscribe.Phase.PRE)
            .isCancelled();
    return cancelled ? HookResult.BREAK : HookResult.CONTINUE;
  }
}
