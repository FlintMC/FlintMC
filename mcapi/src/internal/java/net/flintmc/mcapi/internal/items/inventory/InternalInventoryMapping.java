package net.flintmc.mcapi.internal.items.inventory;

import net.flintmc.mcapi.items.inventory.InventoryDimension;
import net.flintmc.mcapi.items.inventory.InventoryType;

import java.util.function.IntFunction;

public class InternalInventoryMapping {

  private final InventoryType inventoryType;
  private final IntFunction<InventoryDimension> dimensionProvider;

  private InternalInventoryMapping(
      InventoryType inventoryType, IntFunction<InventoryDimension> dimensionProvider) {
    this.dimensionProvider = dimensionProvider;
    this.inventoryType = inventoryType;
  }

  public static InternalInventoryMapping create(InventoryType inventoryType) {
    return create(inventoryType, InventoryDimension::other);
  }

  public static InternalInventoryMapping create(
      InventoryType inventoryType, IntFunction<InventoryDimension> dimensionProvider) {
    return new InternalInventoryMapping(inventoryType, dimensionProvider);
  }

  public InventoryDimension createDimension(int slotCount) {
    return this.dimensionProvider.apply(slotCount);
  }

  public InventoryType getInventoryType() {
    return this.inventoryType;
  }
}
