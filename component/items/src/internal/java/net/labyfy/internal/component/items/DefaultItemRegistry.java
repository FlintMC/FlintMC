package net.labyfy.internal.component.items;

import com.google.common.base.Preconditions;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.component.items.ItemRegistry;
import net.labyfy.component.items.meta.enchantment.EnchantmentType;
import net.labyfy.component.items.type.ItemCategory;
import net.labyfy.component.items.type.ItemType;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.labyfy.component.stereotype.NameSpacedKey;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class DefaultItemRegistry implements ItemRegistry {

  protected final ItemType.Factory itemFactory;
  protected final EnchantmentType.Factory enchantmentFactory;
  protected final MinecraftComponentMapper componentMapper;
  protected final ComponentBuilder.Factory componentFactory;
  protected final ResourceLocationProvider resourceLocationProvider;

  private final ItemType airType;

  private final Map<NameSpacedKey, ItemCategory> itemCategories = new HashMap<>();
  private final Map<NameSpacedKey, ItemType> itemTypes = new HashMap<>();
  private final Map<NameSpacedKey, EnchantmentType> enchantmentTypes = new HashMap<>();

  public DefaultItemRegistry(ItemType.Factory itemFactory, EnchantmentType.Factory enchantmentFactory,
                             MinecraftComponentMapper componentMapper, ComponentBuilder.Factory componentFactory,
                             ResourceLocationProvider resourceLocationProvider) {
    this.itemFactory = itemFactory;
    this.enchantmentFactory = enchantmentFactory;
    this.componentMapper = componentMapper;
    this.componentFactory = componentFactory;
    this.resourceLocationProvider = resourceLocationProvider;

    this.airType = itemFactory.newBuilder().category(null).registryName(NameSpacedKey.minecraft("air")).build();
    this.registerItems();
  }

  protected abstract void registerItems();

  @Override
  public void registerType(ItemType type) {
    Preconditions.checkArgument(!this.itemTypes.containsKey(type.getRegistryName()), "A type with the name %s is already registered", type.getRegistryName());
    this.itemTypes.put(type.getRegistryName(), type);
  }

  @Override
  public ItemType getType(NameSpacedKey registryName) {
    if (registryName.equals(this.airType.getRegistryName())) {
      return this.airType;
    }
    return this.itemTypes.get(registryName);
  }

  @Override
  public ItemType[] getTypes() {
    return this.itemTypes.values().toArray(new ItemType[0]);
  }

  @Override
  public ItemType getAirType() {
    return this.airType;
  }

  @Override
  public ItemType[] getTypesInCategory(ItemCategory category) {
    Collection<ItemType> types = new HashSet<>();

    for (ItemType type : this.itemTypes.values()) {
      //   no category                                     or  the exact category
      if ((category == null && type.getCategory() == null) || (type.getCategory() != null && type.getCategory().equals(category))) {
        types.add(type);
      }
    }

    return types.toArray(new ItemType[0]);
  }

  @Override
  public void registerCategory(ItemCategory category) {
    Preconditions.checkArgument(!this.itemCategories.containsKey(category.getRegistryName()), "A category with the name %s is already registered", category.getRegistryName());
    this.itemCategories.put(category.getRegistryName(), category);
  }

  @Override
  public ItemCategory[] getCategories() {
    return this.itemCategories.values().toArray(new ItemCategory[0]);
  }

  @Override
  public ItemCategory getCategory(int index) {
    for (ItemCategory category : this.itemCategories.values()) {
      if (category.getIndex() == index) {
        return category;
      }
    }
    return null;
  }

  @Override
  public ItemCategory getCategory(NameSpacedKey registryName) {
    return this.itemCategories.get(registryName);
  }

  @Override
  public void registerEnchantmentType(EnchantmentType type) {
    Preconditions.checkArgument(!this.enchantmentTypes.containsKey(type.getRegistryName()), "An enchantment type with the name %s is already registered", type.getRegistryName());
    this.enchantmentTypes.put(type.getRegistryName(), type);
  }

  @Override
  public EnchantmentType[] getEnchantmentTypes() {
    return this.enchantmentTypes.values().toArray(new EnchantmentType[0]);
  }

  @Override
  public EnchantmentType getEnchantmentType(NameSpacedKey registryName) {
    return this.enchantmentTypes.get(registryName);
  }
}
