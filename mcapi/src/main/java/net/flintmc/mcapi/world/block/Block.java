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

package net.flintmc.mcapi.world.block;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.type.ItemType;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.world.math.BlockPosition;

public interface Block {

  <T> T getHandle();

  BlockPosition getPosition();

  BlockState getState();

  boolean isNormalCube();

  boolean isSuffocating();

  boolean causesSuffocation();

  boolean allowsMovement(PathType type);

  float getBlockHardness();

  boolean needsPostProcessing();

  boolean hasSolidSideOnTop();

  boolean isOpaqueCube();

  boolean isEmissiveRendering();

  boolean propagatesSkylightDown();

  int getOpacity();

  float getRelativeBlockHardness(PlayerEntity entity, BlockPosition position);

  int getComparatorInputOverride();

  ItemStack createItemStack();

  ItemType getItemType();

  @AssistedFactory(Block.class)
  interface Factory {

    Block create(@Assisted BlockPosition position);

  }

}
