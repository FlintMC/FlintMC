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

package net.flintmc.mcapi.v1_15_2.tileentity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.tileentity.TileEntity;
import net.flintmc.mcapi.tileentity.type.TileEntityType;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.minecraft.util.math.BlockPos;

@Implement(TileEntity.class)
public class VersionedTileEntity implements TileEntity {

  private final TileEntityType tileEntityType;
  private final World world;
  private final net.minecraft.tileentity.TileEntity tileEntity;

  @AssistedInject
  public VersionedTileEntity(
      @Assisted("tileEntity") Object tileEntity,
      @Assisted("tileEntityType") TileEntityType tileEntityType,
      World world) {
    this.tileEntityType = tileEntityType;
    this.tileEntity = (net.minecraft.tileentity.TileEntity) tileEntity;
    this.world = world;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public World getWorld() {
    return this.world;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasWorld() {
    return this.tileEntity.hasWorld();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getDistanceSq(double x, double y, double z) {
    return this.tileEntity.getDistanceSq(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getMaxRenderDistanceSquared() {
    return this.tileEntity.getMaxRenderDistanceSquared();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockPosition getPosition() {
    return this.world.fromMinecraftBlockPos(this.tileEntity.getPos());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPosition(BlockPosition position) {
    this.tileEntity.setPos((BlockPos) this.world.toMinecraftBlockPos(position));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRemoved() {
    return this.tileEntity.isRemoved();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removed() {
    this.tileEntity.remove();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void validate() {
    this.tileEntity.validate();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateContainingBlockInfo() {
    this.tileEntity.updateContainingBlockInfo();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TileEntityType getType() {
    return this.tileEntityType;
  }
}
