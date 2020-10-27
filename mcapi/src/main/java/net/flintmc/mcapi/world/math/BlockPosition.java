package net.flintmc.mcapi.world.math;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/** Represents the Minecraft BlockPos. */
public interface BlockPosition extends Vector3I {

  /** A factory class for the {@link BlockPosition} */
  @AssistedFactory(BlockPosition.class)
  interface Factory {

    /**
     * Creates a new {@link BlockPosition} with the given coordinates.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @return A created block position.
     */
    BlockPosition create(@Assisted("x") int x, @Assisted("y") int y, @Assisted("z") int z);
  }
}
