package net.labyfy.items.inventory;

import com.google.common.base.Preconditions;
import net.labyfy.component.stereotype.NameSpacedKey;
import net.labyfy.items.inventory.player.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public abstract class DefaultInventoryController implements InventoryController {

  private final PlayerInventory playerInventory;

  private final Map<NameSpacedKey, InventoryType> inventoryTypes = new HashMap<>();

  public DefaultInventoryController(InventoryType playerInventoryType) {
    this.registerType(playerInventoryType);
    this.playerInventory = (PlayerInventory) playerInventoryType.newInventory();
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
}
