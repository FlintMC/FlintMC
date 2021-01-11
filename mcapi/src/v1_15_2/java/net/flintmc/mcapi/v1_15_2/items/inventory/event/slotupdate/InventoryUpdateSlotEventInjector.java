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

package net.flintmc.mcapi.v1_15_2.items.inventory.event.slotupdate;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.PreSubscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.event.DirectionalEvent;
import net.flintmc.mcapi.event.TickEvent;
import net.flintmc.mcapi.items.ItemRegistry;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.InventoryClick;
import net.flintmc.mcapi.items.inventory.InventoryController;
import net.flintmc.mcapi.items.inventory.event.InventoryClickEvent;
import net.flintmc.mcapi.items.inventory.event.InventoryUpdateSlotEvent;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;
import net.flintmc.mcapi.server.event.PacketEvent;
import net.flintmc.transform.hook.Hook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.play.server.SSetSlotPacket;

@Singleton
public class InventoryUpdateSlotEventInjector {

  private final EventBus eventBus;
  private final InventoryController controller;
  private final MinecraftItemMapper itemMapper;
  private final SlotUpdateHandlingItemList.Factory listFactory;
  private final InventoryUpdateSlotEvent.Factory eventFactory;
  private final ItemStack airStack;

  @Inject
  public InventoryUpdateSlotEventInjector(
      EventBus eventBus,
      InventoryController controller,
      MinecraftItemMapper itemMapper,
      SlotUpdateHandlingItemList.Factory listFactory,
      InventoryUpdateSlotEvent.Factory eventFactory,
      ItemRegistry registry) {
    this.eventBus = eventBus;
    this.controller = controller;
    this.itemMapper = itemMapper;
    this.listFactory = listFactory;
    this.eventFactory = eventFactory;
    this.airStack = registry.getAirType().createStack();
  }

  @PreSubscribe(version = "1.15.2")
  public void injectUpdatingItemLists(TickEvent event) {
    ClientPlayerEntity player = Minecraft.getInstance().player;
    if (player != null) {
      this.injectLists(player.inventory);
    }
  }

  @Hook(
      className = "net.minecraft.entity.player.PlayerInventory",
      methodName = "setInventorySlotContents",
      parameters = {@Type(reference = int.class), @Type(typeName = "net.minecraft.item.ItemStack")},
      version = "1.15.2")
  public void setInventorySlotContents(@Named("instance") Object instance) {
    this.injectLists((PlayerInventory) instance);
  }

  private void injectLists(PlayerInventory playerInventory) {
    if (playerInventory.mainInventory instanceof SlotUpdateHandlingItemList) {
      return;
    }

    AccessiblePlayerInventory inventory = (AccessiblePlayerInventory) playerInventory;
    inventory.setMainInventory(this.listFactory.create(9, 36, inventory.getMainInventory()));
    inventory.setArmorInventory(this.listFactory.create(5, 4, inventory.getArmorInventory()));
    inventory.setOffHandInventory(this.listFactory.create(45, 1, inventory.getOffHandInventory()));
    inventory.updateAllInventories();
  }

  @Subscribe(phase = Phase.ANY, version = "1.15.2")
  public void handleSetSlot(PacketEvent event, Phase phase) {
    if (event.getDirection() != DirectionalEvent.Direction.RECEIVE
        || !(event.getPacket() instanceof SSetSlotPacket)
        || !this.controller.canOpenInventories()) {
      return;
    }

    SSetSlotPacket packet = (SSetSlotPacket) event.getPacket();

    if (packet.getWindowId() == this.controller.getPlayerInventory().getWindowId()) {
      // handled by the SlotUpdateHandlingItemList, see above
      return;
    }

    Inventory inventory =
        packet.getWindowId() == this.controller.getOpenInventory().getWindowId()
            ? this.controller.getOpenInventory()
            : null;
    if (inventory == null) {
      return;
    }

    int slot = packet.getSlot();
    ItemStack newItem = this.itemMapper.fromMinecraft(packet.getStack());

    this.eventBus.fireEvent(this.eventFactory.create(inventory, slot, newItem), phase);
  }

  @Subscribe(phase = Phase.ANY, version = "1.15.2")
  public void handleInventoryClick(
      InventoryClickEvent event, ItemStack.Factory itemFactory, Phase phase) {
    // only drops/pickups are not confirmed by the server before updated and therefore not updated
    // above
    InventoryClick type = event.getClickType();

    if (type != InventoryClick.DROP
        && type != InventoryClick.DROP_ALL
        && type != InventoryClick.PICKUP_HALF
        && type != InventoryClick.PICKUP_ALL) {
      return;
    }

    ItemStack clicked = event.getClickedItem();
    if (clicked == null) {
      return;
    }

    if (type != InventoryClick.PICKUP_HALF && clicked.getStackSize() == 1) {
      // update already sent by the server
      return;
    }

    ItemStack newItem;
    switch (phase) {
      case POST:
        newItem = clicked;
        break;

      case PRE:
        if (type == InventoryClick.DROP_ALL || clicked.getStackSize() == 1) {
          newItem = this.airStack;
        } else {
          newItem =
              itemFactory.createItemStack(
                  clicked.getType(), clicked.getStackSize() - 1, clicked.getItemMeta());
        }
        break;

      default:
        throw new IllegalStateException("Unexpected value: " + phase);
    }

    this.eventBus.fireEvent(
        this.eventFactory.create(event.getInventory(), event.getSlot(), newItem), phase);
  }
}
