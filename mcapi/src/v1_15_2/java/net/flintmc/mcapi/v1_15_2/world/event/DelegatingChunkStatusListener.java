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
  private DefaultWorldLoadEvent event;

  private boolean running;
  private int loadedChunks;
  private final int totalChunks;

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
