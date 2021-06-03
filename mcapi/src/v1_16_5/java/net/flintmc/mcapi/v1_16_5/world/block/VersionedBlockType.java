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
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.world.block.BlockState;
import net.flintmc.mcapi.world.block.BlockType;
import net.minecraft.block.Block;

@Implement(BlockType.class)
public class VersionedBlockType implements BlockType {

  private final ResourceLocation name;
  private  BlockState defaultState;

  private ChatComponent displayName;

  private final Block handle;

  private final MinecraftComponentMapper componentMapper;
  private final BlockState.Factory stateFactory;

  @AssistedInject
  public VersionedBlockType(
      BlockState.Factory stateFactory,
      MinecraftComponentMapper componentMapper,
      @Assisted ResourceLocation name,
      @Assisted("handle") Object handle) {
    this.stateFactory = stateFactory;
    this.componentMapper  = componentMapper;
    this.name = name;
    this.handle = (Block) handle;
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
  public ResourceLocation getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockState getDefaultState() {
    if(this.defaultState == null) {
      this.defaultState = this.stateFactory.create(this, this.handle.getDefaultState());
    }
    
    return this.defaultState;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasTileEntity() {
    return this.handle.isTileEntityProvider();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getExplosionResistance() {
    return this.handle.getExplosionResistance();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getDisplayName() {
    if(this.displayName == null) {
      this.displayName = this.componentMapper.fromMinecraft(this.handle.getTranslatedName());
    }

    return this.displayName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getSlipperiness() {
    return this.handle.getSlipperiness();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getSpeedFactor() {
    return this.handle.getSpeedFactor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getJumpFactor() {
    return this.handle.getJumpFactor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.handle.toString();
  }
}
