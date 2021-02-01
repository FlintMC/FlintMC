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

package net.flintmc.mcapi.v1_16_5.world;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.tileentity.TileEntity;
import net.flintmc.mcapi.tileentity.mapper.TileEntityMapper;
import net.flintmc.mcapi.world.World;
import net.flintmc.transform.hook.Hook;
import net.minecraft.util.math.BlockPos;

/**
 * 1.16.5 implementation nof the world interceptor.
 */
@Singleton
public class VersionedWorldInterceptor {

  private final World world;
  private final TileEntityMapper tileEntityMapper;

  @Inject
  private VersionedWorldInterceptor(World world, TileEntityMapper tileEntityMapper) {
    this.world = world;
    this.tileEntityMapper = tileEntityMapper;
  }

  @Hook(
      className = "net.minecraft.world.World",
      methodName = "addTileEntity",
      parameters = {@Type(reference = net.minecraft.tileentity.TileEntity.class)},
      version = "1.16.5")
  public void hookAfterAddTileEntity(@Named("args") Object[] args) {
    net.minecraft.tileentity.TileEntity minecraftTileEntity =
        (net.minecraft.tileentity.TileEntity) args[0];

    if (minecraftTileEntity instanceof net.minecraft.tileentity.SignTileEntity) {
      net.minecraft.tileentity.SignTileEntity signTileEntity =
          (net.minecraft.tileentity.SignTileEntity) minecraftTileEntity;
      this.world
          .addTileEntity(this.tileEntityMapper.fromMinecraftSignTileEntity(signTileEntity));
    } else {
      this.world
          .addTileEntity(this.tileEntityMapper.fromMinecraftTileEntity(minecraftTileEntity));
    }
  }

  @Hook(
      className = "net.minecraft.world.World",
      methodName = "removeTileEntity",
      parameters = {@Type(reference = BlockPos.class)},
      version = "1.16.5")
  public void hookAfterRemoveTileEntity(@Named("args") Object[] args) {
    BlockPos blockPos = (BlockPos) args[0];
    TileEntity tileEntity = this.world.getTileEntity(this.world.fromMinecraftBlockPos(blockPos));

    if (tileEntity != null) {
      this.world.removeTileEntity(tileEntity);
    }
  }
}
