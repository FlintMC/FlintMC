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

package net.flintmc.mcapi.v1_15_2.world.event;

import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.mcapi.internal.world.event.DefaultWorldLoadEvent;
import net.flintmc.mcapi.world.event.WorldLoadEvent.State;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.listener.IChunkStatusListener;

public class DelegatingChunkStatusListener implements IChunkStatusListener {

  private final EventBus eventBus;
  private final IChunkStatusListener delegate;
  private final int totalChunks;
  private DefaultWorldLoadEvent event;
  private boolean running;
  private int loadedChunks;

  public DelegatingChunkStatusListener(IChunkStatusListener delegate, String worldName) {
    this.eventBus = InjectionHolder.getInjectedInstance(EventBus.class);
    this.event = new DefaultWorldLoadEvent(worldName, State.START, 0F);
    this.delegate = delegate;
    this.totalChunks = 23;
    // 23 = (11 * 2) + 1 | renderDistance 11 | see calls to IChunkStatusListenerFactory.create
  }

  @Override
  public void start(ChunkPos center) {
    this.delegate.start(center);
  }

  @Override
  public void statusChanged(ChunkPos chunkPosition, ChunkStatus newStatus) {
    if (this.event != null) {
      if (!this.running) {
        this.running = true;
        this.event.setType(State.START);
        this.eventBus.fireEvent(this.event, Phase.POST);
      }

      if (this.running && newStatus == ChunkStatus.FULL && this.event != null) {
        ++this.loadedChunks;
        float percent = (float) this.loadedChunks * 100F / (float) this.totalChunks;

        this.event.setType(percent == 100 ? State.END : State.UPDATE);
        this.event.setProcessPercentage(percent);
        this.eventBus.fireEvent(this.event, Phase.POST);

        if (percent == 100) {
          this.running = false;
          this.event = null;
        }
      }
    }

    this.delegate.statusChanged(chunkPosition, newStatus);
  }

  @Override
  public void stop() {
    this.delegate.stop();
  }
}
