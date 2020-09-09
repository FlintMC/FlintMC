package net.labyfy.internal.component.items.inventory.type;

import com.google.common.base.Preconditions;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.items.inventory.InventoryDimension;
import net.labyfy.component.items.inventory.InventoryType;
import net.labyfy.component.stereotype.NameSpacedKey;

public class DefaultInventoryType implements InventoryType {

  private final NameSpacedKey registryName;
  private final ChatComponent defaultTitle;
  private final InventoryDimension defaultDimension;
  private final boolean customizableDimensions;
  private final Inventory.Factory inventoryFactory;

  public DefaultInventoryType(NameSpacedKey registryName, ChatComponent defaultTitle, InventoryDimension defaultDimension, boolean customizableDimensions, Inventory.Factory inventoryFactory) {
    this.registryName = registryName;
    this.defaultTitle = defaultTitle;
    this.defaultDimension = defaultDimension;
    this.customizableDimensions = customizableDimensions;
    this.inventoryFactory = inventoryFactory;
  }

  @Override
  public NameSpacedKey getRegistryName() {
    return this.registryName;
  }

  @Override
  public ChatComponent getDefaultTitle() {
    return this.defaultTitle;
  }

  @Override
  public InventoryDimension getDefaultDimension() {
    return this.defaultDimension;
  }

  @Override
  public boolean isCustomizableDimensions() {
    return this.customizableDimensions;
  }

  @Override
  public Inventory newInventory() {
    return this.newInventory(this.getDefaultTitle(), this.getDefaultDimension());
  }

  @Override
  public Inventory newInventory(ChatComponent title) {
    return this.newInventory(title, this.getDefaultDimension());
  }

  @Override
  public Inventory newInventory(InventoryDimension dimension) {
    return this.newInventory(this.getDefaultTitle(), dimension);
  }

  @Override
  public Inventory newInventory(ChatComponent title, InventoryDimension dimension) {
    if (!this.customizableDimensions) {
      Preconditions.checkArgument(this.defaultDimension.equals(dimension), "Invalid dimension");
    }

    return this.inventoryFactory.createInventory(this, title, dimension);
  }
}
