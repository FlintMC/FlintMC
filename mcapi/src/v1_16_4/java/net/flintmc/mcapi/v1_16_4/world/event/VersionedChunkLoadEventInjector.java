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

package net.flintmc.mcapi.v1_16_4.world.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.world.event.ChunkLoadEvent;
import net.flintmc.mcapi.world.event.ChunkUnloadEvent;
import net.flintmc.transform.hook.Hook;
import net.minecraft.world.chunk.Chunk;

@Singleton
public class VersionedChunkLoadEventInjector {

  private final EventBus eventBus;
  private final ChunkLoadEvent.Factory loadEventFactory;
  private final ChunkUnloadEvent.Factory unloadEventFactory;

  @Inject
  private VersionedChunkLoadEventInjector(
      EventBus eventBus,
      ChunkLoadEvent.Factory loadEventFactory,
      ChunkUnloadEvent.Factory unloadEventFactory) {
    this.eventBus = eventBus;
    this.loadEventFactory = loadEventFactory;
    this.unloadEventFactory = unloadEventFactory;
  }

  @Hook(
      className = "net.minecraft.client.multiplayer.ClientChunkProvider",
      methodName = "loadChunk",
      parameters = {
          @Type(reference = int.class), // chunkX
          @Type(reference = int.class), // chunkZ
          @Type(typeName = "net.minecraft.world.biome.BiomeContainer"),
          @Type(typeName = "net.minecraft.network.PacketBuffer"),
          @Type(typeName = "net.minecraft.nbt.CompoundNBT"),
          @Type(reference = int.class) // size
      },
      executionTime = Hook.ExecutionTime.BEFORE)
  public void preChunkLoad(@Named("args") Object[] args) {
    int chunkX = (int) args[0];
    int chunkZ = (int) args[1];

    ChunkLoadEvent event = this.loadEventFactory.create(chunkX, chunkZ);
    this.eventBus.fireEvent(event, Subscribe.Phase.PRE);
  }

  @Hook(
      className = "net.minecraft.client.world.ClientWorld",
      methodName = "onChunkLoaded",
      parameters = {
          @Type(reference = int.class), // chunkX
          @Type(reference = int.class) // chunkZ
      },
      executionTime = Hook.ExecutionTime.AFTER)
  public void postChunkLoad(@Named("args") Object[] args) {
    int chunkX = (int) args[0];
    int chunkZ = (int) args[1];

    ChunkLoadEvent event = this.loadEventFactory.create(chunkX, chunkZ);
    this.eventBus.fireEvent(event, Subscribe.Phase.POST);
  }

  @Hook(
      className = "net.minecraft.client.multiplayer.ClientChunkProvider$ChunkArray",
      methodName = "unload",
      parameters = {
          @Type(reference = int.class), // chunkIndex
          @Type(typeName = "net.minecraft.world.chunk.Chunk"),
          @Type(typeName = "net.minecraft.world.chunk.Chunk")
      },
      executionTime = Hook.ExecutionTime.BEFORE)
  public void preChunkUnload(@Named("args") Object[] args) {
    Chunk chunk = (Chunk) args[1];
    int chunkX = chunk.getPos().x;
    int chunkZ = chunk.getPos().z;

    ChunkUnloadEvent event = this.unloadEventFactory.create(chunkX, chunkZ);
    this.eventBus.fireEvent(event, Subscribe.Phase.PRE);
  }

  @Hook(
      className = "net.minecraft.client.world.ClientWorld",
      methodName = "onChunkUnloaded",
      parameters = @Type(typeName = "net.minecraft.world.chunk.Chunk"),
      executionTime = Hook.ExecutionTime.AFTER)
  public void postChunkUnload(@Named("args") Object[] args) {
    Chunk chunk = (Chunk) args[0];
    int chunkX = chunk.getPos().x;
    int chunkZ = chunk.getPos().z;

    ChunkUnloadEvent event = this.unloadEventFactory.create(chunkX, chunkZ);
    this.eventBus.fireEvent(event, Subscribe.Phase.POST);
  }
}
