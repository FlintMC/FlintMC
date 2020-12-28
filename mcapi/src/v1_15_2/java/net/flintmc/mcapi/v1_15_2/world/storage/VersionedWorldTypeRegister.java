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
import java.util.List;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.mapper.WorldMapper;
import net.flintmc.mcapi.world.type.WorldType;
import net.flintmc.mcapi.world.type.WorldTypeRegister;
import net.flintmc.render.gui.event.OpenGLInitializeEvent;

@Implement(value = WorldTypeRegister.class, version = "1.15.2")
public class VersionedWorldTypeRegister implements WorldTypeRegister {

  private final WorldMapper worldMapper;
  private final List<WorldType> worldTypes;

  @Inject
  private VersionedWorldTypeRegister(WorldMapper worldMapper) {
    this.worldMapper = worldMapper;
    this.worldTypes = Lists.newArrayList();
  }

  @Subscribe(phase = Subscribe.Phase.POST)
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
}
