package net.flintmc.mcapi.internal.world.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.event.ChunkLoadEvent;

/** {@inheritDoc} */
@Implement(ChunkLoadEvent.class)
public class DefaultChunkLoadEvent implements ChunkLoadEvent {

  private final int x;
  private final int z;

  @AssistedInject
  public DefaultChunkLoadEvent(@Assisted("x") int x, @Assisted("z") int z) {
    this.x = x;
    this.z = z;
  }

  /** {@inheritDoc} */
  @Override
  public int getX() {
    return this.x;
  }

  /** {@inheritDoc} */
  @Override
  public int getZ() {
    return this.z;
  }
}
