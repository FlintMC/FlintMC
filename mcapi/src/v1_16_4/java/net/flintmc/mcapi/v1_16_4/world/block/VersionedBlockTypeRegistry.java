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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.flintmc.mcapi.world.block.BlockType;
import net.flintmc.mcapi.world.block.BlockTypeRegistry;
import net.minecraft.block.Block;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;

@Singleton
@Implement(value = BlockTypeRegistry.class, version = "1.16.4")
public class VersionedBlockTypeRegistry implements BlockTypeRegistry {

  private final Map<ResourceLocation, BlockType> types;

  @Inject
  private VersionedBlockTypeRegistry(
      ResourceLocationProvider provider,
      BlockType.Factory typeFactory) {
    this.types = new HashMap<>();

    for (Block block : Registry.BLOCK) {
      ResourceLocation location = provider.fromMinecraft(Registry.BLOCK.getKey(block));

      BlockType type = typeFactory.create(location, block);
      this.types.put(location, type);
    }
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
