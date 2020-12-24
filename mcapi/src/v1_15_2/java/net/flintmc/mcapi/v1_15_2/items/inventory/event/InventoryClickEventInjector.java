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

package net.flintmc.mcapi.v1_15_2.items.inventory.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.InventoryClick;
import net.flintmc.mcapi.items.inventory.InventoryController;
import net.flintmc.mcapi.items.inventory.event.InventoryClickEvent;
import net.flintmc.mcapi.items.inventory.event.InventoryHotkeyPressEvent;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.HookResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;

@Singleton
public class InventoryClickEventInjector {

  private final EventBus eventBus;
  private final InventoryController controller;
  private final InventoryClickEvent.Factory clickEventFactory;
  private final InventoryHotkeyPressEvent.Factory hotkeyEventFactory;
  private long mergeTimeout = -1;

  @Inject
  private InventoryClickEventInjector(
      EventBus eventBus,
      InventoryController controller,
      InventoryClickEvent.Factory clickEventFactory,
      InventoryHotkeyPressEvent.Factory hotkeyEventFactory) {
    this.eventBus = eventBus;
    this.controller = controller;
    this.clickEventFactory = clickEventFactory;
    this.hotkeyEventFactory = hotkeyEventFactory;
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      className = "net.minecraft.inventory.container.Container",
      methodName = "slotClick",
      parameters = {
          @Type(reference = int.class),
          @Type(reference = int.class),
          @Type(reference = ClickType.class),
          @Type(reference = PlayerEntity.class)
      },
      defaultValue = "net.minecraft.item.ItemStack.EMPTY")
  public HookResult slotClick(
      @Named("instance") Object instance,
      @Named("args") Object[] args,
      Hook.ExecutionTime executionTime) {
    int windowId = ((Container) instance).windowId;
    Inventory inventory =
        this.controller.getPlayerInventory().getWindowId() == windowId
            ? this.controller.getPlayerInventory()
            : this.controller.getOpenInventory().getWindowId() == windowId
                ? this.controller.getOpenInventory()
                : null;
    if (inventory == null) {
      return HookResult.CONTINUE;
    }
    int slot = (int) args[0];
    int button = (int) args[1];
    ClickType type = (ClickType) args[2];

    InventoryClick click = null;
    switch (type) {
      case THROW:
        click = button == 1 ? InventoryClick.DROP_ALL : InventoryClick.DROP;
        break;

      case CLONE:
        click = InventoryClick.CLONE;
        break;

      case PICKUP:
        click = button == 1 ? InventoryClick.PICKUP_HALF : InventoryClick.PICKUP_ALL;
        this.mergeTimeout = System.currentTimeMillis() + 250;
        break;

      case PICKUP_ALL:
        if (this.mergeTimeout != -1 && this.mergeTimeout < System.currentTimeMillis()) {
          click = InventoryClick.MERGE_ALL;
          this.mergeTimeout = -1;
        }
        break;

      case QUICK_MOVE:
        click = InventoryClick.MOVE;
        break;

      case SWAP:
        return this.performHotkeyPress(inventory, slot, button, executionTime);

      default:
        return HookResult.CONTINUE;
    }

    if (click == null) {
      return HookResult.CONTINUE;
    }

    return this.eventBus
        .fireEvent(this.clickEventFactory.create(inventory, slot, click), executionTime)
        .isCancelled()
        ? HookResult.BREAK
        : HookResult.CONTINUE;
  }

  private HookResult performHotkeyPress(
      Inventory inventory, int slot, int hotkey, Hook.ExecutionTime executionTime) {
    return this.eventBus
        .fireEvent(this.hotkeyEventFactory.create(inventory, slot, hotkey), executionTime)
        .isCancelled()
        ? HookResult.BREAK
        : HookResult.CONTINUE;
  }
}
