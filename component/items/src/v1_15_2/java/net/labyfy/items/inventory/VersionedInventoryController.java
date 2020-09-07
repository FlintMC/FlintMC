package net.labyfy.items.inventory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.stereotype.NameSpacedKey;
import net.labyfy.items.ItemRegistry;
import net.labyfy.items.mapper.MinecraftItemMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;

import java.util.HashMap;
import java.util.Map;

@Singleton
@Implement(value = InventoryController.class, version = "1.15.2")
public class VersionedInventoryController extends DefaultInventoryController {

  private final Map<Class<? extends Container>, InventoryType> minecraftMappings = new HashMap<>();

  @Inject
  public VersionedInventoryController(ItemRegistry itemRegistry, ComponentBuilder.Factory componentFactory,
                                      InventoryType.Factory typeFactory, MinecraftItemMapper itemMapper) {
    super(
        typeFactory.newBuilder()
            .registryName(NameSpacedKey.minecraft("player"))
            .defaultDimension(InventoryDimension.other(41))
            .factory((type, title, dimension) -> new VersionedPlayerInventory(itemRegistry, type, dimension, componentFactory, itemMapper))
            .build()
    );

    /*this.registerDefaultType(ChestContainer.class, typeFactory.newBuilder()
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

    this.registerDefaultType(RepairContainer.class, typeFactory.newBuilder().registryName(NameSpacedKey.minecraft("anvil")).defaultDimension(other(3)).build());
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
  public Inventory getOpenInventory() {
    return null;
  }

  @Override
  public void showInventory(Inventory inventory) {
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
