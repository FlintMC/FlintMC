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

package net.flintmc.mcapi.v1_16_4.world.block;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.world.block.BlockState;
import net.flintmc.mcapi.world.block.BlockType;
import net.minecraft.block.Block;

@Implement(value = BlockType.class, version = "1.16.4")
public class VersionedBlockType implements BlockType {

  private final ResourceLocation key;
  private final BlockState defaultState;

  private final ChatComponent displayName;

  private final Block handle;

  @AssistedInject
  public VersionedBlockType(
      BlockState.Factory stateFactory,
      MinecraftComponentMapper componentMapper,
      @Assisted ResourceLocation key,
      @Assisted("handle") Object handle) {
    this.key = key;
    this.handle = (Block) handle;

    this.displayName = componentMapper.fromMinecraft(this.handle.getTranslatedName());

    this.defaultState = stateFactory.create(this, this.handle.getDefaultState());
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
    return this.key;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockState getDefaultState() {
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
