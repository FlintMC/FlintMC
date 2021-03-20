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

package net.flintmc.mcapi.v1_15_2.items.inventory;

import static net.flintmc.mcapi.items.inventory.InventoryDimension.other;
import static net.flintmc.mcapi.items.inventory.InventoryDimension.rect;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.internal.items.inventory.DefaultInventoryController;
import net.flintmc.mcapi.internal.items.inventory.InternalInventoryMapping;
import net.flintmc.mcapi.items.ItemRegistry;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.InventoryClick;
import net.flintmc.mcapi.items.inventory.InventoryController;
import net.flintmc.mcapi.items.inventory.InventoryDimension;
import net.flintmc.mcapi.items.inventory.InventoryType;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.DispenserContainer;
import net.minecraft.inventory.container.PlayerContainer;

@Singleton
@Implement(InventoryController.class)
public class VersionedInventoryController extends DefaultInventoryController {

  private final Map<Class<? extends Container>, InternalInventoryMapping> minecraftMappings =
      new HashMap<>();

  private final ItemRegistry itemRegistry;
  private final MinecraftItemMapper itemMapper;
  private final MinecraftComponentMapper componentMapper;

  @Inject
  public VersionedInventoryController(
      ItemRegistry itemRegistry,
      ComponentBuilder.Factory componentFactory,
      InventoryType.Factory typeFactory,
      MinecraftItemMapper itemMapper,
      MinecraftComponentMapper componentMapper) {
    super(
        new VersionedPlayerInventory(
            itemRegistry,
            typeFactory
                .newBuilder()
                .registryName(NameSpacedKey.minecraft("player"))
                .defaultDimension(other(41))
                .build(),
            other(41),
            componentFactory,
            itemMapper));

    this.itemRegistry = itemRegistry;
    this.itemMapper = itemMapper;
    this.componentMapper = componentMapper;

    this.registerDefaultType(
        ChestContainer.class,
        InternalInventoryMapping.create(
            typeFactory
                .newBuilder()
                .registryName(NameSpacedKey.minecraft("chest"))
                .defaultTitle(componentFactory.translation().translationKey("generic_9x3").build())
                .defaultDimension(rect(9, 3))
                .customizableDimensions()
                .build(),
            slotCount -> InventoryDimension.rect(9, slotCount / 9)));
    this.registerDefaultType(
        DispenserContainer.class,
        typeFactory
            .newBuilder()
            .registryName(NameSpacedKey.minecraft("dispenser"))
            .defaultTitle(componentFactory.translation().translationKey("generic_3x3").build())
            .defaultDimension(rect(3, 3))
            .build());

    /*
    TODO implement more types than the chest and dispenser

    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("anvil")).defaultDimension(other(3)).build());
    this.registerDefaultType(.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("beacon")).defaultDimension(other(3)).build());
    this.registerDefaultType(.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("blast_furnace")).defaultDimension(other(1)).build());
    this.registerDefaultType(.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("brewing_stand")).defaultDimension(other(5)).build());
    this.registerDefaultType(.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("crafting")).defaultDimension(other(10)).build());
    this.registerDefaultType(.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("enchantment")).defaultDimension(other(2)).build());
    this.registerDefaultType(.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("furnace")).defaultDimension(other(3)).build());
    this.registerDefaultType(.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("grindstone")).defaultDimension(other(3)).build());
    this.registerDefaultType(.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("hopper")).defaultDimension(other(5)).build());
    this.registerDefaultType(.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("lectern")).defaultDimension(other(0)).build());
    this.registerDefaultType(.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("loom")).defaultDimension(other(4)).build());
    this.registerDefaultType(.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("merchant")).defaultDimension(other(3)).build());
    this.registerDefaultType(.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("shulker_box")).defaultDimension(rect(9, 3)).build());
    this.registerDefaultType(.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("smoker")).defaultDimension(other(3)).build());
    this.registerDefaultType(.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("cartography_table")).defaultDimension(other(3)).build());
    this.registerDefaultType(.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("stonecutter")).defaultDimension(other(2)).build());*/
  }

  private void registerDefaultType(
      Class<? extends Container> handleClass, InventoryType inventoryType) {
    // any inventory type that can't be customized and is no rectangle
    this.registerDefaultType(handleClass, InternalInventoryMapping.create(inventoryType));
  }

  private void registerDefaultType(
      Class<? extends Container> handleClass, InternalInventoryMapping mapping) {
    super.registerType(mapping.getInventoryType());
    this.minecraftMappings.put(handleClass, mapping);
  }

  @Override
  protected boolean isOpened(Inventory inventory) {
    Container container = Minecraft.getInstance().player.openContainer;
    if (container == null) {
      return false;
    }

    return inventory instanceof VersionedOpenedInventory
        && ((VersionedOpenedInventory) inventory).getContainer().equals(container);
  }

  @Override
  protected Inventory defineOpenInventory() {
    if (!this.canOpenInventories()) {
      return null;
    }

    Container container = Minecraft.getInstance().player.openContainer;
    if (container == null) {
      return null;
    }
    if (container instanceof PlayerContainer) {
      return this.getPlayerInventory();
    }

    InternalInventoryMapping mapping = this.minecraftMappings.get(container.getClass());
    if (mapping == null) {
      return null;
    }
    InventoryType type = mapping.getInventoryType();

    InventoryDimension dimension =
        type.isCustomizableDimensions()
            ? mapping.createDimension(
            container.getInventory().size()
                - Minecraft.getInstance().player.inventory.mainInventory.size())
            : type.getDefaultDimension();

    ChatComponent title = type.getDefaultTitle();
    Screen currentScreen = Minecraft.getInstance().currentScreen;
    if (currentScreen instanceof ContainerScreen && currentScreen.getTitle() != null) {
      title = this.componentMapper.fromMinecraft(currentScreen.getTitle());
    }

    return new VersionedOpenedInventory(
        this.itemRegistry, type, dimension, this.itemMapper, container, title);
  }

  @Override
  public boolean canOpenInventories() {
    return Minecraft.getInstance().player != null
        && Minecraft.getInstance().player.inventory != null;
  }

  @Override
  public boolean hasInventoryOpened() {
    return Minecraft.getInstance().currentScreen instanceof ContainerScreen;
  }

  @Override
  public void performClick(InventoryClick click, int slot) {
    Preconditions.checkArgument(
        this.hasInventoryOpened(), "Cannot click into the inventory if no inventory is opened");

    switch (click) {
      case DROP_ALL:
        this.clickWindow(slot, 1, ClickType.THROW);
        break;

      case DROP:
        this.clickWindow(slot, 0, ClickType.THROW);
        break;

      case CLONE:
        this.clickWindow(slot, 2, ClickType.CLONE);
        break;

      case PICKUP_ALL:
        this.clickWindow(slot, 0, ClickType.PICKUP);
        break;

      case PICKUP_HALF:
        this.clickWindow(slot, 1, ClickType.PICKUP);
        break;

      case MOVE:
        this.clickWindow(slot, 0, ClickType.QUICK_MOVE);
        break;

      case MERGE_ALL:
        this.clickWindow(slot, 0, ClickType.PICKUP);
        this.clickWindow(slot, 0, ClickType.PICKUP_ALL);
        break;

      default:
        throw new IllegalArgumentException("Unknown click: " + click);
    }
  }

  @Override
  public void performHotkeyPress(int hotkey, int slot) throws IndexOutOfBoundsException {
    Preconditions.checkArgument(
        this.hasInventoryOpened(), "Cannot click into the inventory if no inventory is opened");

    if (hotkey < 0 || hotkey > 8) {
      throw new IllegalArgumentException(
          "Invalid hotkey provided: " + hotkey + " (Not in range 0 - 8)");
    }

    this.clickWindow(slot, hotkey, ClickType.SWAP);
  }

  private void clickWindow(int slot, int button, ClickType type) {
    int windowId =
        ((ContainerScreen<?>) Minecraft.getInstance().currentScreen).getContainer().windowId;

    Minecraft.getInstance()
        .playerController
        .windowClick(windowId, slot, button, type, Minecraft.getInstance().player);
  }
}
