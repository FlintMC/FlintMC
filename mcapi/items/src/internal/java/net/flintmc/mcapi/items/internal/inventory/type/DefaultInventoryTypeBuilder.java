package net.flintmc.mcapi.items.internal.inventory.type;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.inventory.InventoryDimension;
import net.flintmc.mcapi.items.inventory.InventoryType;
import net.flintmc.framework.stereotype.NameSpacedKey;

@Implement(InventoryType.Builder.class)
public class DefaultInventoryTypeBuilder implements InventoryType.Builder {

  private final ComponentBuilder.Factory componentFactory;

  private NameSpacedKey registryName;
  private ChatComponent defaultTitle;
  private InventoryDimension defaultDimension;
  private boolean customizableDimensions;

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
  public InventoryType build() {
    Preconditions.checkNotNull(this.registryName, "Missing registryName");
    Preconditions.checkNotNull(this.defaultDimension, "Missing dimension");

    if (this.defaultTitle == null) {
      this.defaultTitle = this.componentFactory.translation().translationKey(this.registryName.getKey()).build();
    }

    return new DefaultInventoryType(this.registryName, this.defaultTitle, this.defaultDimension, this.customizableDimensions);
  }
}
