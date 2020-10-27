package net.flintmc.mcapi.items.internal.inventory;

import com.google.common.base.Preconditions;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.InventoryController;
import net.flintmc.mcapi.items.inventory.InventoryType;
import net.flintmc.mcapi.items.inventory.player.PlayerInventory;
import net.flintmc.framework.stereotype.NameSpacedKey;

import java.util.HashMap;
import java.util.Map;

public abstract class DefaultInventoryController implements InventoryController {

  private final PlayerInventory playerInventory;

  private final Map<NameSpacedKey, InventoryType> inventoryTypes = new HashMap<>();

  private Inventory openInventory;

  public DefaultInventoryController(PlayerInventory playerInventory) {
    this.registerType(playerInventory.getType());
    this.playerInventory = playerInventory;
  }

  @Override
  public InventoryType[] getTypes() {
    return this.inventoryTypes.values().toArray(new InventoryType[0]);
  }

  @Override
  public InventoryType getType(NameSpacedKey registryName) {
    return this.inventoryTypes.get(registryName);
  }

  @Override
  public void registerType(InventoryType type) {
    Preconditions.checkArgument(!this.inventoryTypes.containsKey(type.getRegistryName()), "A type with the name %s is already registered", type.getRegistryName());
    this.inventoryTypes.put(type.getRegistryName(), type);
  }

  @Override
  public PlayerInventory getPlayerInventory() {
    return this.canOpenInventories() ? this.playerInventory : null;
  }

  @Override
  public Inventory getOpenInventory() {
    if (this.openInventory == null || !this.isOpened(this.openInventory)) {
      this.openInventory = this.defineOpenInventory();
    }
    return this.openInventory;
  }

  protected abstract boolean isOpened(Inventory inventory);

  protected abstract Inventory defineOpenInventory();

}
