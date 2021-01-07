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
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.InventoryController;
import net.flintmc.mcapi.items.inventory.event.InventoryHeldItemChangeEvent.Factory;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;
import net.flintmc.transform.hook.HookResult;
import net.minecraft.client.Minecraft;

@Singleton
public class InventoryHeldItemChangeEventInjector {

  private final EventBus eventBus;
  private final InventoryController controller;
  private final Factory eventFactory;

  @Inject
  private InventoryHeldItemChangeEventInjector(
      EventBus eventBus, InventoryController controller, Factory eventFactory) {
    this.eventBus = eventBus;
    this.controller = controller;
    this.eventFactory = eventFactory;
  }

  @Hook(
      className = "net.minecraft.client.multiplayer.PlayerController",
      methodName = "syncCurrentPlayItem",
      executionTime = ExecutionTime.BEFORE)
  public HookResult fireHeldItemChangeEvent(
      @Named("instance") Object instance, ExecutionTime executionTime) {
    int knownSlot = ((AccessiblePlayerController) instance).getCurrentPlayerItem();
    int changedSlot = Minecraft.getInstance().player.inventory.currentItem;
    if (knownSlot != changedSlot) {
      ItemStack item = this.controller.getPlayerInventory().getItem(changedSlot + 36);
      boolean cancelled =
          this.eventBus
              .fireEvent(this.eventFactory.create(changedSlot, item), executionTime)
              .isCancelled();

      return cancelled ? HookResult.BREAK : HookResult.CONTINUE;
    }
    return HookResult.CONTINUE;
  }
}
