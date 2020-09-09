package net.labyfy.internal.component.items;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.ItemRegistry;
import net.labyfy.component.items.meta.ItemMeta;
import net.labyfy.component.items.type.ItemCategory;
import net.labyfy.component.items.type.ItemType;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.labyfy.component.stereotype.NameSpacedKey;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

@Singleton
@Implement(value = ItemRegistry.class, version = "1.15.2")
public class VersionedItemRegistry extends DefaultItemRegistry {

  @Inject
  public VersionedItemRegistry(ItemType.Factory itemFactory, MinecraftComponentMapper componentMapper,
                               ComponentBuilder.Factory componentFactory, ResourceLocationProvider resourceLocationProvider) {
    super(itemFactory, componentMapper, componentFactory, resourceLocationProvider);
  }

  @Override
  protected void registerItems() {
    Map<NameSpacedKey, Class<? extends ItemMeta>> specialMetaClasses = this.getSpecialItemMetaClasses();

    // create one builder for every item
    ItemType.Builder builder = super.itemFactory.newBuilder();

    for (Item item : Registry.ITEM) {
      // name in the registry like minecraft:stone
      ResourceLocation resourceLocation = Registry.ITEM.getKey(item);

      NameSpacedKey registryName = NameSpacedKey.of(resourceLocation.getNamespace(), resourceLocation.getPath());

      // creative mode categories
      ItemCategory category = this.findCategory(registryName, item);

      // copy all values of the minecraft item to our builder
      builder
          .category(category)
          .registryName(registryName)
          .defaultDisplayName(super.componentFactory.translation().translationKey(item.getTranslationKey()).build())
          .maxDamage(item.getMaxDamage())
          .maxStackSize(item.getMaxStackSize());

      // exceptions for the ItemMeta like leather armor
      if (specialMetaClasses.containsKey(registryName)) {
        builder.metaClass(specialMetaClasses.get(registryName));
      }

      super.registerType(builder.build());

      // re-use the builder for every other item in the registry
      builder.reset();
    }
  }

  private Map<NameSpacedKey, Class<? extends ItemMeta>> getSpecialItemMetaClasses() {
    Map<NameSpacedKey, Class<? extends ItemMeta>> exceptions = new HashMap<>();

    // TODO add metas like the leather armor with the configurable color

    return exceptions;
  }

  private ItemCategory findCategory(NameSpacedKey itemRegistryName, Item item) {
    if (item.getGroup() == null || item.getGroup() == ItemGroup.SEARCH) {
      // items like the barrier don't have a category in the creative menu
      return null;
    }

    NameSpacedKey registryName = NameSpacedKey.minecraft(item.getGroup().getPath());

    ItemCategory category = super.getCategory(registryName);
    if (category != null) {
      return category;
    }

    category = ItemCategory.create(registryName,
        this.componentFactory.translation().translationKey(item.getGroup().getTranslationKey()).build(),
        () -> super.getType(itemRegistryName),
        item.getGroup().getIndex()
    );

    super.registerCategory(category);
    return category;
  }

}
