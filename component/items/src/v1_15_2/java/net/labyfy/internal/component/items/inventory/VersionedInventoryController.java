package net.labyfy.internal.component.items.inventory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.ItemRegistry;
import net.labyfy.component.items.inventory.*;
import net.labyfy.component.items.mapper.MinecraftItemMapper;
import net.labyfy.component.stereotype.NameSpacedKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.DispenserContainer;

import java.util.HashMap;
import java.util.Map;

import static net.labyfy.component.items.inventory.InventoryDimension.other;
import static net.labyfy.component.items.inventory.InventoryDimension.rect;

@Singleton
@Implement(value = InventoryController.class, version = "1.15.2")
public class VersionedInventoryController extends DefaultInventoryController {

  private final Map<Class<? extends Container>, InventoryType> minecraftMappings = new HashMap<>();

  private final ItemRegistry itemRegistry;
  private final MinecraftItemMapper itemMapper;
  private final MinecraftComponentMapper componentMapper;

  @Inject
  public VersionedInventoryController(ItemRegistry itemRegistry, ComponentBuilder.Factory componentFactory,
                                      InventoryType.Factory typeFactory, MinecraftItemMapper itemMapper, MinecraftComponentMapper componentMapper) {
    super(new VersionedPlayerInventory(
        itemRegistry,
        typeFactory.newBuilder()
            .registryName(NameSpacedKey.minecraft("player"))
            .defaultDimension(other(41))
            .build(),
        other(41),
        componentFactory,
        itemMapper)
    );

    this.itemRegistry = itemRegistry;
    this.itemMapper = itemMapper;
    this.componentMapper = componentMapper;

    this.registerDefaultType(ChestContainer.class, typeFactory.newBuilder()
        .registryName(NameSpacedKey.minecraft("chest"))
        .defaultTitle(componentFactory.translation().translationKey("generic_9x3").build())
        .defaultDimension(rect(9, 3))
        .customizableDimensions()
        .build());
    this.registerDefaultType(DispenserContainer.class, typeFactory.newBuilder()
        .registryName(NameSpacedKey.minecraft("dispenser"))
        .defaultTitle(componentFactory.translation().translationKey("generic_3x3").build())
        .defaultDimension(rect(3, 3))
        .build());

    /*this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("anvil")).defaultDimension(other(3)).build());
    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("beacon")).defaultDimension(other(3)).build());
    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("blast_furnace")).defaultDimension(other(1)).build());
    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("brewing_stand")).defaultDimension(other(5)).build());
    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("crafting")).defaultDimension(other(10)).build());
    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("enchantment")).defaultDimension(other(2)).build());
    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("furnace")).defaultDimension(other(3)).build());
    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("grindstone")).defaultDimension(other(3)).build());
    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("hopper")).defaultDimension(other(5)).build());
    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("lectern")).defaultDimension(other(0)).build());
    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("loom")).defaultDimension(other(4)).build());
    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("merchant")).defaultDimension(other(3)).build());
    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("shulker_box")).defaultDimension(rect(9, 3)).build());
    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("smoker")).defaultDimension(other(3)).build());
    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("cartography_table")).defaultDimension(other(3)).build());
    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("stonecutter")).defaultDimension(other(2)).build());*/
  }

  private void registerDefaultType(Class<? extends Container> handleClass, InventoryType type) {
    super.registerType(type);
    this.minecraftMappings.put(handleClass, type);
  }

  @Override
  protected boolean isOpened(Inventory inventory) {
    Container container = Minecraft.getInstance().player.openContainer;
    if (container == null) {
      return false;
    }

    return inventory instanceof VersionedInventory && ((VersionedInventory) inventory).getContainer().equals(container);
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

    InventoryType type = this.minecraftMappings.get(container.getClass());
    if (type == null) {
      return null;
    }

    InventoryDimension dimension = type.isCustomizableDimensions() ?
        other(container.getInventory().size() - this.getPlayerInventory().getDimension().getSlotCount()) : // TODO this can also be a rect
        type.getDefaultDimension();

    ChatComponent title = type.getDefaultTitle();
    Screen currentScreen = Minecraft.getInstance().currentScreen;
    if (currentScreen instanceof ContainerScreen && currentScreen.getTitle() != null) {
      title = this.componentMapper.fromMinecraft(currentScreen.getTitle());
    }

    return new VersionedInventory(this.itemRegistry, container.windowId, type, dimension, this.itemMapper, () -> container, title);
  }

  @Override
  public boolean canOpenInventories() {
    return Minecraft.getInstance().player != null && Minecraft.getInstance().player.inventory != null;
  }

  @Override
  public void performClick(InventoryClick click, int slot) {
  }

  @Override
  public void performHotkeyPress(int hotkey, int slot) throws IndexOutOfBoundsException {
  }

}
