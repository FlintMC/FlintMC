package net.labyfy.component.items.type;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.stereotype.NameSpacedKey;

import java.util.function.Supplier;

public class ItemCategory {

  private final NameSpacedKey registryName;
  private final ChatComponent displayName;
  private final int index;
  private final Supplier<ItemType> iconSupplier;

  private boolean showTitle;

  private ItemCategory(NameSpacedKey registryName, ChatComponent displayName, Supplier<ItemType> iconSupplier, int index) {
    this.registryName = registryName;
    this.displayName = displayName;
    this.index = index;
    this.showTitle = true;
    this.iconSupplier = iconSupplier;
  }

  public static ItemCategory create(NameSpacedKey registryName, ChatComponent displayName, Supplier<ItemType> iconSupplier, int index) {
    return new ItemCategory(registryName, displayName, iconSupplier, index);
  }

  public NameSpacedKey getRegistryName() {
    return this.registryName;
  }

  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  public int getIndex() {
    return this.index;
  }

  public ItemType createIcon() {
    return this.iconSupplier.get();
  }

  public boolean isShowTitle() {
    return this.showTitle;
  }

  public void setShowTitle(boolean showTitle) {
    this.showTitle = showTitle;
  }
}
