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

package net.flintmc.mcapi.v1_15_2.tileentity.type;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.tileentity.type.TileEntityType;
import net.flintmc.mcapi.tileentity.type.TileEntityTypeRegister;
import net.flintmc.render.gui.event.OpenGLInitializeEvent;
import net.minecraft.util.registry.Registry;

@Singleton
@Implement(value = TileEntityTypeRegister.class, version = "1.15.2")
public class VersionedTileEntityTypeRegister implements TileEntityTypeRegister {

  private final Map<String, TileEntityType> tileEntityTypes;
  private final TileEntityType.Factory tileEntityTypeFactory;

  @Inject
  private VersionedTileEntityTypeRegister(TileEntityType.Factory tileEntityTypeFactory) {
    this.tileEntityTypeFactory = tileEntityTypeFactory;
    this.tileEntityTypes = Maps.newHashMap();
  }

  @Subscribe(phase = Subscribe.Phase.POST, version = "1.15.2")
  public void convertTileEntityTypes(OpenGLInitializeEvent event) {
    for (net.minecraft.tileentity.TileEntityType<?> tileEntityType : Registry.BLOCK_ENTITY_TYPE) {
      String key = Registry.BLOCK_ENTITY_TYPE.getKey(tileEntityType).getPath();
      this.tileEntityTypes.put(key, this.tileEntityTypeFactory.create());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, TileEntityType> getTileEntitiesTypes() {
    return this.tileEntityTypes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TileEntityType getTileEntityType(String key) {
    return this.tileEntityTypes.get(key);
  }
}
