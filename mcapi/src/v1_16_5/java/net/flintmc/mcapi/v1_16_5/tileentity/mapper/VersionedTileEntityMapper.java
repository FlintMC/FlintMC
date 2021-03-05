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

package net.flintmc.mcapi.v1_16_5.tileentity.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.internal.tileentity.cache.TileEntityCache;
import net.flintmc.mcapi.tileentity.SignTileEntity;
import net.flintmc.mcapi.tileentity.TileEntity;
import net.flintmc.mcapi.tileentity.mapper.TileEntityMapper;
import net.flintmc.mcapi.tileentity.type.TileEntityTypeRegister;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

@Singleton
@Implement(TileEntityMapper.class)
public class VersionedTileEntityMapper implements TileEntityMapper {

  private final TileEntityCache tileEntityCache;
  private final TileEntity.Factory tileEntityFactory;
  private final TileEntityTypeRegister tileEntityTypeRegister;
  private final SignTileEntity.Factory signTileEntityFactory;
  private final World world;

  @Inject
  private VersionedTileEntityMapper(
      TileEntityCache tileEntityCache,
      TileEntity.Factory tileEntityFactory,
      TileEntityTypeRegister tileEntityTypeRegister,
      SignTileEntity.Factory signTileEntityFactory,
      World world) {
    this.tileEntityCache = tileEntityCache;
    this.tileEntityFactory = tileEntityFactory;
    this.tileEntityTypeRegister = tileEntityTypeRegister;
    this.signTileEntityFactory = signTileEntityFactory;
    this.world = world;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftTileEntity(TileEntity tileEntity) {

    for (net.minecraft.tileentity.TileEntity entity :
        Minecraft.getInstance().world.loadedTileEntityList) {
      if (entity instanceof net.minecraft.tileentity.SignTileEntity
          && equalsBlockPosition(tileEntity.getPosition(), entity.getPos())) {
        return entity;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TileEntity fromMinecraftTileEntity(Object tileEntity) {
    if (!(tileEntity instanceof net.minecraft.tileentity.TileEntity)) {
      throw new IllegalArgumentException(
          tileEntity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.tileentity.TileEntity.class.getName());
    }

    net.minecraft.tileentity.TileEntity minecraftTileEntity =
        (net.minecraft.tileentity.TileEntity) tileEntity;

    BlockPosition blockPosition = this.world.fromMinecraftBlockPos(minecraftTileEntity.getPos());

    if (minecraftTileEntity instanceof net.minecraft.tileentity.SignTileEntity) {

      return this.tileEntityCache.putIfAbsent(
          blockPosition, () -> fromMinecraftSignTileEntity(minecraftTileEntity));
    } else {

      return this.tileEntityCache.putIfAbsent(
          blockPosition,
          () ->
              this.tileEntityFactory.create(
                  minecraftTileEntity,
                  this.tileEntityTypeRegister.getTileEntityType(
                      Registry.BLOCK_ENTITY_TYPE.getKey(minecraftTileEntity.getType()).getPath())));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftSignTileEntity(SignTileEntity signTileEntity) {

    for (net.minecraft.tileentity.TileEntity tileEntity :
        Minecraft.getInstance().world.loadedTileEntityList) {
      if (tileEntity instanceof net.minecraft.tileentity.SignTileEntity
          && equalsBlockPosition(signTileEntity.getPosition(), tileEntity.getPos())) {
        return tileEntity;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SignTileEntity fromMinecraftSignTileEntity(Object signTileEntity) {
    if (!(signTileEntity instanceof net.minecraft.tileentity.SignTileEntity)) {
      throw new IllegalArgumentException(
          signTileEntity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.tileentity.SignTileEntity.class.getName());
    }

    net.minecraft.tileentity.SignTileEntity minecraftSignTileEntity =
        (net.minecraft.tileentity.SignTileEntity) signTileEntity;

    BlockPosition blockPosition =
        this.world.fromMinecraftBlockPos(minecraftSignTileEntity.getPos());

    return (SignTileEntity)
        this.tileEntityCache.putIfAbsent(
            blockPosition, () -> this.signTileEntityFactory.create(minecraftSignTileEntity));
  }

  private boolean equalsBlockPosition(BlockPosition position, BlockPos blockPos) {
    return position.getX() == blockPos.getX()
        && position.getY() == blockPos.getY()
        && position.getZ() == blockPos.getZ();
  }
}
