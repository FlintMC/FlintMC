/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.v1_15_2.entity.item;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.item.ItemEntity;
import net.flintmc.mcapi.entity.item.ItemEntityMapper;
import net.flintmc.mcapi.internal.entity.DefaultEntityRepository;
import net.minecraft.client.Minecraft;

@Singleton
@Implement(value = ItemEntityMapper.class, version = "1.15.2")
public class VersionedItemEntityMapper implements ItemEntityMapper {

  private final DefaultEntityRepository entityRepository;
  private final ItemEntity.Factory itemEntityFactory;

  @Inject
  private VersionedItemEntityMapper(DefaultEntityRepository entityRepository, ItemEntity.Factory itemEntityFactory) {
    this.entityRepository = entityRepository;
    this.itemEntityFactory = itemEntityFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemEntity fromMinecraftItemEntity(Object handle) {
    if (!(handle instanceof net.minecraft.entity.item.ItemEntity)) {
      throw new IllegalArgumentException(
          handle.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.item.ItemEntity.class.getName());
    }

    net.minecraft.entity.item.ItemEntity itemEntity = (net.minecraft.entity.item.ItemEntity) handle;

    return (ItemEntity)
        this.entityRepository.putIfAbsent(
            itemEntity.getUniqueID(), () -> this.itemEntityFactory.create(itemEntity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftItemEntity(ItemEntity itemEntity) {
    for (net.minecraft.entity.Entity allEntity : Minecraft.getInstance().world.getAllEntities()) {
      if (allEntity instanceof net.minecraft.entity.item.ItemEntity
          && allEntity.getEntityId() == itemEntity.getIdentifier()) {
        return allEntity;
      }
    }
    return null;
  }
}
