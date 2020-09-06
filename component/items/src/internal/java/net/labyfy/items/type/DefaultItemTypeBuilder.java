package net.labyfy.items.type;

import com.google.common.base.Preconditions;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.labyfy.component.stereotype.NameSpacedKey;
import net.labyfy.items.meta.ItemMeta;

@Implement(ItemType.Builder.class)
public class DefaultItemTypeBuilder implements ItemType.Builder {

  private final ComponentBuilder.Factory componentFactory;
  private final ItemMeta.Factory metaFactory;
  private final ResourceLocationProvider resourceLocationProvider;
  private ItemCategory category;
  private NameSpacedKey registryName;
  private ChatComponent defaultDisplayName;
  private int maxStackSize;
  private int maxDamage;
  private Class<? extends ItemMeta> metaClass;
  private ResourceLocation resourceLocation;

  @AssistedInject
  public DefaultItemTypeBuilder(ComponentBuilder.Factory componentFactory, ItemMeta.Factory metaFactory, ResourceLocationProvider resourceLocationProvider) {
    this.componentFactory = componentFactory;
    this.metaFactory = metaFactory;
    this.resourceLocationProvider = resourceLocationProvider;
    this.reset();
  }

  @Override
  public ItemType.Builder category(ItemCategory category) {
    this.category = category;
    return this;
  }

  @Override
  public ItemType.Builder registryName(NameSpacedKey registryName) {
    this.registryName = registryName;
    return this;
  }

  @Override
  public ItemType.Builder defaultDisplayName(ChatComponent defaultDisplayName) {
    this.defaultDisplayName = defaultDisplayName;
    return this;
  }

  @Override
  public ItemType.Builder maxStackSize(int maxStackSize) {
    this.maxStackSize = maxStackSize;
    return this;
  }

  @Override
  public ItemType.Builder maxDamage(int maxDamage) {
    this.maxDamage = maxDamage;
    return this;
  }

  @Override
  public ItemType.Builder metaClass(Class<? extends ItemMeta> metaClass) {
    this.metaClass = metaClass;
    return this;
  }

  @Override
  public ItemType.Builder resourceLocation(ResourceLocation resourceLocation) {
    this.resourceLocation = resourceLocation;
    return this;
  }

  @Override
  public ItemType build() {
    Preconditions.checkNotNull(this.registryName, "Missing registry name");
    Preconditions.checkNotNull(this.metaClass, "Missing meta class");
    Preconditions.checkArgument(this.maxStackSize > 0, "MaxStackSize cannot be zero or smaller");

    if (this.defaultDisplayName == null) {
      this.defaultDisplayName = this.componentFactory.text().text(this.registryName.getKey()).build();
    }
    if (this.maxDamage <= 0) {
      this.maxDamage = -1;
    }
    if (this.resourceLocation == null) {
      this.resourceLocation = this.resourceLocationProvider.get(this.registryName.getNameSpace(), this.registryName.getKey());
    }

    return new DefaultItemType(this.metaFactory, this.category, this.registryName, this.defaultDisplayName, this.maxStackSize, this.maxDamage, this.metaClass, this.resourceLocation);
  }

  @Override
  public void reset() {
    // reset everything to their default value
    this.category = null;
    this.registryName = null;
    this.defaultDisplayName = null;
    this.maxStackSize = 1;
    this.maxDamage = -1;
    this.metaClass = ItemMeta.class;
    this.resourceLocation = null;
  }
}
