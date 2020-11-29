package net.flintmc.mcapi.internal.world.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.event.ChunkUnloadEvent;

@Implement(ChunkUnloadEvent.class)
public class DefaultChunkUnloadEvent implements ChunkUnloadEvent {

  private final int x;
  private final int z;

  @AssistedInject
  public DefaultChunkUnloadEvent(@Assisted("x") int x, @Assisted("z") int z) {
    this.x = x;
    this.z = z;
  }

  @Override
  public int getX() {
    return this.x;
  }

  @Override
  public int getZ() {
    return this.z;
  }
}
