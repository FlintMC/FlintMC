package net.labyfy.items.inventory;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.stereotype.NameSpacedKey;

public interface InventoryType {

  NameSpacedKey getRegistryName();

  ChatComponent getDefaultTitle();

  InventoryDimension getDefaultDimension();

  boolean isCustomizableDimensions();

  Inventory newInventory();

  Inventory newInventory(ChatComponent title);

  Inventory newInventory(InventoryDimension dimension);

  Inventory newInventory(ChatComponent title, InventoryDimension dimension);

  interface Builder {

    Builder registryName(NameSpacedKey registryName);

    Builder defaultTitle(ChatComponent defaultTitle);

    Builder defaultDimension(InventoryDimension dimension);

    Builder customizableDimensions();

    Builder factory(Inventory.Factory factory);

    InventoryType build();

  }

  @AssistedFactory(Builder.class)
  interface Factory {

    Builder newBuilder();

  }

}
