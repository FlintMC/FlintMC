package net.labyfy.items.inventory.type;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.stereotype.NameSpacedKey;
import net.labyfy.items.inventory.Inventory;
import net.labyfy.items.inventory.InventoryDimension;
import net.labyfy.items.inventory.InventoryType;

@Implement(InventoryType.Builder.class)
public class DefaultInventoryTypeBuilder implements InventoryType.Builder {

  private final ComponentBuilder.Factory componentFactory;

  private NameSpacedKey registryName;
  private ChatComponent defaultTitle;
  private InventoryDimension defaultDimension;
  private boolean customizableDimensions;
  private Inventory.Factory inventoryFactory;

  @Inject
  public DefaultInventoryTypeBuilder(ComponentBuilder.Factory componentFactory) {
    this.componentFactory = componentFactory;
  }

  @Override
  public InventoryType.Builder registryName(NameSpacedKey registryName) {
    this.registryName = registryName;
    return this;
  }

  @Override
  public InventoryType.Builder defaultTitle(ChatComponent defaultTitle) {
    this.defaultTitle = defaultTitle;
    return this;
  }

  @Override
  public InventoryType.Builder defaultDimension(InventoryDimension defaultDimension) {
    this.defaultDimension = defaultDimension;
    return this;
  }

  @Override
  public InventoryType.Builder customizableDimensions() {
    this.customizableDimensions = true;
    return this;
  }

  @Override
  public InventoryType.Builder factory(Inventory.Factory factory) {
    this.inventoryFactory = factory;
    return this;
  }

  @Override
  public InventoryType build() {
    Preconditions.checkNotNull(this.registryName, "Missing registryName");
    Preconditions.checkNotNull(this.defaultDimension, "Missing dimension");
    Preconditions.checkNotNull(this.inventoryFactory, "Missing factory");

    if (this.defaultTitle == null) {
      this.defaultTitle = this.componentFactory.translation().translationKey(this.registryName.getKey()).build();
    }

    return new DefaultInventoryType(this.registryName, this.defaultTitle, this.defaultDimension, this.customizableDimensions, this.inventoryFactory);
  }
}
