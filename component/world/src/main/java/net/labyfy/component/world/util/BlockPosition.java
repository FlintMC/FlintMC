package net.labyfy.component.world.util;

/**
 * Represents the Minecraft BlockPos.
 */
public interface BlockPosition extends Vector3I {

  interface Factory {

    /**
     * Creates a new {@link BlockPosition} with the given coordinates.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @return A created block position.
     */
    BlockPosition create(int x, int y, int z);

  }

}
