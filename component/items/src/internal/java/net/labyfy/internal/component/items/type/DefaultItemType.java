package net.labyfy.internal.component.items.type;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.meta.ItemMeta;
import net.labyfy.component.items.type.ItemCategory;
import net.labyfy.component.items.type.ItemType;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.stereotype.NameSpacedKey;
import net.labyfy.internal.component.items.DefaultItemStack;

public class DefaultItemType implements ItemType {

  private final ItemMeta.Factory metaFactory;
  private final ItemCategory category;
  private final NameSpacedKey registryName;
  private final ChatComponent defaultDisplayName;
  private final int maxStackSize;
  private final int maxDamage;
  private final Class<? extends ItemMeta> metaClass;
  private final ResourceLocation resourceLocation;

  public DefaultItemType(ItemMeta.Factory metaFactory, ItemCategory category, NameSpacedKey registryName,
                         ChatComponent defaultDisplayName, int maxStackSize, int maxDamage,
                         Class<? extends ItemMeta> metaClass, ResourceLocation resourceLocation) {
    this.metaFactory = metaFactory;
    this.category = category;
    this.registryName = registryName;
    this.defaultDisplayName = defaultDisplayName;
    this.maxStackSize = maxStackSize;
    this.maxDamage = maxDamage;
    this.metaClass = metaClass != null ? metaClass : ItemMeta.class;
    this.resourceLocation = resourceLocation;
  }

  @Override
  public ItemCategory getCategory() {
    return this.category;
  }

  @Override
  public NameSpacedKey getRegistryName() {
    return this.registryName;
  }

  @Override
  public ChatComponent getDefaultDisplayName() {
    return this.defaultDisplayName;
  }

  @Override
  public int getMaxStackSize() {
    return this.maxStackSize;
  }

  @Override
  public int getMaxDamage() {
    return this.maxDamage;
  }

  @Override
  public boolean isDamageable() {
    return this.maxDamage > 0;
  }

  @Override
  public Class<? extends ItemMeta> getMetaClass() {
    return this.metaClass;
  }

  @Override
  public ItemStack createStack() {
    return this.createStack(1);
  }

  @Override
  public ItemStack createStack(int stackSize) {
    return new DefaultItemStack(this.metaFactory, this, stackSize);
  }

  @Override
  public ResourceLocation getResourceLocation() {
    return this.resourceLocation;
  }

}
