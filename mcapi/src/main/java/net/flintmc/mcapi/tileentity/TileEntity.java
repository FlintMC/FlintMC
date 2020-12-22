package net.flintmc.mcapi.tileentity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.tileentity.type.TileEntityType;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.math.BlockPosition;

/**
 * Represents the Minecraft tile entity.
 */
public interface TileEntity {

  /**
   * Retrieves the world of this tile entity.
   *
   * @return The tile entity world.
   */
  World getWorld();

  /**
   * Whether the tile entity has a world.
   *
   * @return {@code true} if the tile entity has a world, otherwise {@code false}.
   */
  boolean hasWorld();

  /**
   * Retrieves the squared distance of the given parameters.
   *
   * @param x The `x` position.
   * @param y The `y` position.
   * @param z The `z` position.
   * @return The calculated squared distance.
   */
  double getDistanceSq(double x, double y, double z);

  /**
   * Retrieves the maximal render squared distance of this tile entity.
   *
   * @return The tile entity maximal render squared distance.
   */
  double getMaxRenderDistanceSquared();

  /**
   * Retrieves the block position of this tile entity.
   *
   * @return The tile entity block position.
   */
  BlockPosition getPosition();

  /**
   * Changes the block position of this tile entity.
   *
   * @param position The new block position.
   */
  void setPosition(BlockPosition position);

  /**
   * Whether the tile entity is removed.
   *
   * @return {@code true} if the tile entity is removed, otherwise {@code false}.
   */
  boolean isRemoved();

  /**
   * Removes the tile entity.
   */
  void removed();

  /**
   * Validates the tile entity.
   */
  void validate();

  /**
   * Updates the containing block information of this tile entity.
   */
  void updateContainingBlockInfo();

  /**
   * Retrieves the type of this tile entity.
   *
   * @return The tile entity type.
   */
  TileEntityType getType();

  /**
   * A factory class for the {@link TileEntity}.
   */
  @AssistedFactory(TileEntity.class)
  interface Factory {

    /**
     * Creates a new {@link TileEntity} with the given parameters.
     *
     * @param tileEntity     The non-null Minecraft tile entity.
     * @param tileEntityType The type of the tile entity.
     * @return A created tile entity.
     */
    TileEntity create(
        @Assisted("tileEntity") Object tileEntity,
        @Assisted("tileEntityType") TileEntityType tileEntityType);
  }
}
