package net.labyfy.internal.component.items;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.meta.ItemMeta;
import net.labyfy.component.items.type.ItemType;

@Implement(ItemStack.class)
public class DefaultItemStack implements ItemStack {

  private final ItemMeta.Factory metaFactory;
  private final ItemType type;
  private final int stackSize;
  private ItemMeta meta;

  @AssistedInject
  public DefaultItemStack(ItemMeta.Factory metaFactory, @Assisted("type") ItemType type, @Assisted("stackSize") int stackSize) {
    this.metaFactory = metaFactory;
    this.type = type;
    this.stackSize = stackSize;
  }

  @AssistedInject
  public DefaultItemStack(ItemMeta.Factory metaFactory, @Assisted("type") ItemType type, @Assisted("stackSize") int stackSize, @Assisted("meta") ItemMeta meta) {
    this.metaFactory = metaFactory;
    this.type = type;
    this.stackSize = stackSize;
    this.meta = meta;
  }

  @Override
  public boolean hasItemMeta() {
    return this.meta != null;
  }

  @Override
  public ItemMeta getItemMeta(boolean create) {
    if (create && this.meta == null) {
      this.meta = this.metaFactory.createMeta(this.type);
    }
    return this.meta;
  }

  @Override
  public int getStackSize() {
    return this.stackSize;
  }

  @Override
  public ItemType getType() {
    return this.type;
  }

}
