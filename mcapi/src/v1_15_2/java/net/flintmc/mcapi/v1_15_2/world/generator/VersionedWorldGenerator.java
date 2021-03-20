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

package net.flintmc.mcapi.v1_15_2.world.generator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.ServerController;
import net.flintmc.mcapi.world.generator.WorldGenerator;
import net.flintmc.mcapi.world.generator.WorldGeneratorBuilder;
import net.flintmc.mcapi.world.generator.WorldGeneratorMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.WorldSettings;

@Singleton
@Implement(WorldGenerator.class)
public class VersionedWorldGenerator implements WorldGenerator {

  private final ServerController serverController;
  private final WorldGeneratorMapper mapper;

  @Inject
  private VersionedWorldGenerator(ServerController serverController, WorldGeneratorMapper mapper) {
    this.serverController = serverController;
    this.mapper = mapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void generateAndJoin(WorldGeneratorBuilder builder) {
    builder.validate();

    WorldSettings handle = (WorldSettings) this.mapper.toMinecraftGenerator(builder);

    this.serverController.disconnectFromServer();

    Minecraft.getInstance().launchIntegratedServer(builder.findFileName(), builder.name(), handle);
  }
}
