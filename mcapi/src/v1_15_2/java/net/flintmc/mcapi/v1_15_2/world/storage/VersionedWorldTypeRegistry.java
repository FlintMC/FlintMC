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

package net.flintmc.mcapi.v1_15_2.world.storage;

import com.beust.jcommander.internal.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.List;
import net.flintmc.framework.eventbus.event.subscribe.PostSubscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.mapper.WorldMapper;
import net.flintmc.mcapi.world.type.WorldType;
import net.flintmc.mcapi.world.type.WorldTypeRegistry;
import net.flintmc.render.gui.event.OpenGLInitializeEvent;

@Singleton
@Implement(value = WorldTypeRegistry.class, version = "1.15.2")
public class VersionedWorldTypeRegistry implements WorldTypeRegistry {

  private final WorldMapper worldMapper;
  private final List<WorldType> worldTypes;

  @Inject
  private VersionedWorldTypeRegistry(WorldMapper worldMapper) {
    this.worldMapper = worldMapper;
    this.worldTypes = Lists.newArrayList();
  }

  @PostSubscribe(version = "1.15.2")
  public void loadWorldTypes(OpenGLInitializeEvent event) {
    for (net.minecraft.world.WorldType worldType : net.minecraft.world.WorldType.WORLD_TYPES) {
      if (worldType != null) {
        this.worldTypes.add(this.worldMapper.fromMinecraftWorldType(worldType));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<WorldType> getWorldTypes() {
    return this.worldTypes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldType getDefaultType() {
    return this.getType("default");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldType getFlatType() {
    return this.getType("flat");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldType getLargeBiomesType() {
    return this.getType("largeBiomes");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldType getAmplifiedType() {
    return this.getType("amplified");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldType getBuffetType() {
    return this.getType("buffet");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldType getType(String name) {
    for (WorldType type : this.worldTypes) {
      if (type.getName().equals(name)) {
        return type;
      }
    }
    return null;
  }
}
