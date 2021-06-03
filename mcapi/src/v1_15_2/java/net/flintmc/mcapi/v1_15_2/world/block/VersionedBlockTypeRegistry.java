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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.event.subscribe.PreSubscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.registry.RegistryRegisterEvent;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.world.block.BlockType;
import net.flintmc.mcapi.world.block.BlockTypeRegistry;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Implement(BlockTypeRegistry.class)
public class VersionedBlockTypeRegistry implements BlockTypeRegistry {

  private final Map<ResourceLocation, BlockType> types;

  @Inject
  private VersionedBlockTypeRegistry() {
    this.types = new HashMap<>();
  }

  @PreSubscribe
  public void registerBlocks(final RegistryRegisterEvent event, final BlockType.Factory typeFactory) {

    if (!event.getRegistryKeyLocation().getPath().equals("block")) {
      return;
    }

    ResourceLocation blockResourceLocation =  event.getRegistryValueLocation();
    BlockType blockType = typeFactory.create(blockResourceLocation, event.getRegistryObject());
    this.types.put(blockResourceLocation, blockType);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<BlockType> getTypes() {
    return this.types.values();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockType getType(ResourceLocation location) {
    return this.types.get(location);
  }
}
