package net.labyfy.internal.component.entity.v1_15_2.item;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.item.ItemEntity;
import net.labyfy.component.entity.item.ItemEntityMapper;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.internal.component.entity.cache.EntityCache;
import net.minecraft.client.Minecraft;

@Singleton
@Implement(value = ItemEntityMapper.class, version = "1.15.2")
public class VersionedItemEntityMapper implements ItemEntityMapper {

  private final EntityCache entityCache;
  private final ItemEntity.Factory itemEntityFactory;

  @Inject
  private VersionedItemEntityMapper(EntityCache entityCache, ItemEntity.Factory itemEntityFactory) {
    this.entityCache = entityCache;
    this.itemEntityFactory = itemEntityFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemEntity fromMinecraftItemEntity(Object handle) {
    if (!(handle instanceof net.minecraft.entity.item.ItemEntity)) {
      throw new IllegalArgumentException(handle.getClass().getName() + " is not an instance of " + net.minecraft.entity.item.ItemEntity.class.getName());
    }

    net.minecraft.entity.item.ItemEntity itemEntity = (net.minecraft.entity.item.ItemEntity) handle;

    return (ItemEntity) this.entityCache.putIfAbsent(
            itemEntity.getUniqueID(),
            () -> this.itemEntityFactory.create(itemEntity)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftItemEntity(ItemEntity itemEntity) {
    for (net.minecraft.entity.Entity allEntity : Minecraft.getInstance().world.getAllEntities()) {
      if (allEntity instanceof net.minecraft.entity.item.ItemEntity && allEntity.getEntityId() == itemEntity.getIdentifier()) {
        return allEntity;
      }
    }
    return null;
  }
}
