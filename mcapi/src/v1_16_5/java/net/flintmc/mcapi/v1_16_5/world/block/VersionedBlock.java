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

package net.flintmc.mcapi.v1_16_5.world.block;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.entity.mapper.EntityMapper;
import net.flintmc.mcapi.items.ItemRegistry;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;
import net.flintmc.mcapi.items.type.ItemType;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.block.Block;
import net.flintmc.mcapi.world.block.BlockState;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

@Implement(Block.class)
public class VersionedBlock implements Block {

  private final EntityMapper entityMapper;
  private final MinecraftItemMapper itemMapper;
  private final ItemRegistry itemRegistry;

  private final BlockPosition position;
  private final BlockState state;
  private final BlockPos positionHandle;
  private final net.minecraft.block.BlockState handle;

  private ItemType resolvedItemType;

  @AssistedInject
  public VersionedBlock(
      EntityMapper entityMapper,
      MinecraftItemMapper itemMapper,
      ItemRegistry itemRegistry,
      World world,
      @Assisted BlockPosition position) {
    this.entityMapper = entityMapper;
    this.itemMapper = itemMapper;
    this.itemRegistry = itemRegistry;
    this.position = position;
    this.positionHandle = (BlockPos) world.toMinecraftBlockPos(position);

    this.state = world.getBlockState(position);
    this.handle = this.state.getHandle();
  }

  private net.minecraft.world.World getWorld() {
    net.minecraft.world.World world = Minecraft.getInstance().world;
    if (world == null) {
      throw new IllegalStateException("Cannot use functions of the block while not in a world");
    }
    return world;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> T getHandle() {
    return (T) this.handle.getBlock();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockPosition getPosition() {
    return this.position;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockState getState() {
    return this.state;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getBlockHardness() {
    return this.handle.getBlockHardness(this.getWorld(), this.positionHandle);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getComparatorInputOverride() {
    return this.handle.getComparatorInputOverride(this.getWorld(), this.positionHandle);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack createItemStack() {
    Object itemStack = this.handle.getBlock().getItem(
        this.getWorld(), this.positionHandle, this.handle);
    return this.itemMapper.fromMinecraft(itemStack);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemType getItemType() {
    if (this.resolvedItemType != null) {
      return this.resolvedItemType;
    }

    Item item = this.handle.getBlock().asItem();
    ResourceLocation key = Registry.ITEM.getKey(item);

    return this.resolvedItemType =
        this.itemRegistry.getType(NameSpacedKey.of(key.getNamespace(), key.getPath()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.handle.toString() + "[" + this.position + "]";
  }
}
