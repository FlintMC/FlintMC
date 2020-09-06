package net.labyfy.items.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.stereotype.NameSpacedKey;
import net.labyfy.items.ItemRegistry;
import net.labyfy.items.ItemStack;
import net.labyfy.items.meta.ItemMeta;
import net.labyfy.items.type.ItemType;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

@Singleton
@Implement(value = MinecraftItemMapper.class, version = "1.15.2")
public class DefaultMinecraftItemMapper implements MinecraftItemMapper {

  private final ItemRegistry registry;
  private final ItemStack.Factory itemFactory;
  private final ItemMeta.Factory metaFactory;

  @Inject
  public DefaultMinecraftItemMapper(ItemRegistry registry, ItemStack.Factory itemFactory, ItemMeta.Factory metaFactory) {
    this.registry = registry;
    this.itemFactory = itemFactory;
    this.metaFactory = metaFactory;
  }

  @Override
  public ItemStack fromMinecraft(Object handle) throws ItemMappingException {
    if (!(handle instanceof net.minecraft.item.ItemStack)) {
      throw new ItemMappingException(handle.getClass().getName() + " is not an instance of " + net.minecraft.item.ItemStack.class.getName());
    }
    net.minecraft.item.ItemStack stack = (net.minecraft.item.ItemStack) handle;

    ResourceLocation resourceLocation = Registry.ITEM.getKey(stack.getItem());
    NameSpacedKey registryName = NameSpacedKey.of(resourceLocation.getNamespace(), resourceLocation.getPath());

    ItemType type = this.registry.getType(registryName);
    if (type == null) {
      throw new ItemMappingException("No item type with the name " + registryName + " found");
    }

    ItemMeta meta = this.metaFactory.createMeta(type);

    if (stack.getTag() != null) {
      meta.applyNBTFrom(stack.getTag());
    }

    return this.itemFactory.createItemStack(type, stack.getCount(), meta);
  }

  @Override
  public Object toMinecraft(ItemStack stack) throws ItemMappingException {
    Item item = Registry.ITEM.getValue(stack.getType().getResourceLocation().getHandle())
        .orElseThrow(() -> new ItemMappingException("Unknown item " + stack.getType().getResourceLocation()));

    net.minecraft.item.ItemStack result = new net.minecraft.item.ItemStack(() -> item, stack.getStackSize());

    if (stack.hasItemMeta()) {
      result.setTag(new CompoundNBT());
      stack.getItemMeta(false).copyNBTTo(result.getTag());
    }

    return result;
  }
}
