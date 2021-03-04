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

package net.flintmc.mcapi.v1_15_2.world.block;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.flintmc.mcapi.world.block.BlockState;
import net.flintmc.mcapi.world.block.BlockType;
import net.flintmc.mcapi.world.block.BlockTypeRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

@Implement(value = BlockState.class)
public class VersionedBlockState implements BlockState {

  private final BlockType type;
  private final net.minecraft.block.BlockState handle;

  @AssistedInject
  public VersionedBlockState(
      ResourceLocationProvider provider,
      BlockTypeRegistry registry,
      @Assisted("handle") Object handle) {
    this(getType(provider, registry, handle), handle);
  }

  @AssistedInject
  public VersionedBlockState(@Assisted BlockType type, @Assisted("handle") Object handle) {
    this.type = type;
    this.handle = (net.minecraft.block.BlockState) handle;
  }

  private static BlockType getType(
      ResourceLocationProvider provider,
      BlockTypeRegistry registry,
      Object handle) {
    net.minecraft.block.BlockState state = (net.minecraft.block.BlockState) handle;
    ResourceLocation key = Registry.BLOCK.getKey(state.getBlock());

    return registry.getType(provider.fromMinecraft(key));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> T getHandle() {
    return (T) this.handle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockType getType() {
    return this.type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean ticksRandomly() {
    return this.handle.ticksRandomly();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSolid() {
    return this.handle.isSolid();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTransparent() {
    return this.handle.isTransparent();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getLightValue() {
    return this.handle.getLightValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasComparatorInputOverride() {
    return this.handle.hasComparatorInputOverride();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canProvidePower() {
    return this.handle.canProvidePower();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.handle.toString();
  }
}
