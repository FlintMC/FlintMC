package net.labyfy.internal.component.items.inventory.type;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.items.inventory.InventoryDimension;
import net.labyfy.component.items.inventory.InventoryType;
import net.labyfy.component.stereotype.NameSpacedKey;

public class DefaultInventoryType implements InventoryType {

  private final NameSpacedKey registryName;
  private final ChatComponent defaultTitle;
  private final InventoryDimension defaultDimension;
  private final boolean customizableDimensions;

  public DefaultInventoryType(NameSpacedKey registryName, ChatComponent defaultTitle, InventoryDimension defaultDimension, boolean customizableDimensions) {
    this.registryName = registryName;
    this.defaultTitle = defaultTitle;
    this.defaultDimension = defaultDimension;
    this.customizableDimensions = customizableDimensions;
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

}
