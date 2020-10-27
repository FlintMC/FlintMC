package net.flintmc.mcapi.world.border;

import net.flintmc.mcapi.world.math.BlockPosition;

/** Represents the Minecraft world border. */
public interface WorldBorder {

  /**
   * Whether the specified block position contains the min/max size of the border.
   *
   * @param position The block position to be checked.
   * @return {@code true} if the block position contains, otherwise {@code false}
   */
  default boolean contains(BlockPosition position) {
    return (double) (position.getX() + 1) > this.minX()
        && (double) position.getX() < this.maxX()
        && (double) (position.getZ() + 1) > this.minZ()
        && (double) position.getZ() < this.maxZ();
  }

  /**
   * Retrieves the X minimum of this border.
   *
   * @return The X minimum.
   */
  double minX();

  /**
   * Retrieves the Z minimum of this border.
   *
   * @return The Z minimum.
   */
  double minZ();

  /**
   * Retrieves the X maximum of this border.
   *
   * @return The X maximum.
   */
  double maxX();

  /**
   * Retrieves the Z maximum of this border.
   *
   * @return The Z maximum.
   */
  double maxZ();

  /**
   * Retrieves the closest distance of the given points.
   *
   * @param x The X coordinate.
   * @param z The Z coordinate.
   * @return The calculated closest distance.
   */
  default double getClosestDistance(double x, double z) {
    return Math.min(
        Math.min(Math.min(x - this.minX(), this.maxX() - x), z - this.minZ()), this.maxZ() - z);
  }

  /**
   * Retrieves the current state of this border.
   *
   * @return The current state.
   */
  BorderState getState();

  /**
   * Retrieves the X center of this border.
   *
   * @return The X center.
   */
  double getCenterX();

  /**
   * Retrieves the Z center of this border.
   *
   * @return The Z center.
   */
  double getCenterZ();

  /**
   * Changes the center of this border.
   *
   * @param x The new X coordinate.
   * @param z The new Z coordinate.
   */
  void setCenter(double x, double z);

  /**
   * Retrieves the diameter of this border.
   *
   * @return The diameter of this border.
   */
  double getDiameter();

  /**
   * Retrieves the time until target of this border.
   *
   * @return The time until target of this border.
   */
  long getTimeUntilTarget();

  /**
   * Retrieves the target size of this border.
   *
   * @return The target size of this border.
   */
  double getTargetSize();

  /**
   * Changes the transition of this border.
   *
   * @param newSize The new size of this border.
   */
  void setTransition(double newSize);

  /**
   * Sets the transition of this border.
   *
   * @param oldSize The old size of this border.
   * @param newSize The new size of this border.
   * @param duration The duration, how long it takes fro the border to reach the new size.
   */
  void setTransition(double oldSize, double newSize, long duration);

  /**
   * Retrieves the size of this border.
   *
   * @return The border size.
   */
  int getSize();

  /**
   * Changes the size of this border.
   *
   * @param size The new size.
   */
  void setSize(int size);

  /**
   * Retrieves the damage buffer of this border.
   *
   * @return The border damage buffer.
   */
  double getDamageBuffer();

  /**
   * Changes the damage buffer of this border.
   *
   * @param buffer The new damage buffer.
   */
  void setDamageBuffer(double buffer);

  /**
   * Retrieves the damage per block of this border.
   *
   * @return The border damage per block.
   */
  double getDamagePerBlock();

  /**
   * Changes the border damage per block.
   *
   * @param amount The new damage per block.
   */
  void setDamagePerBlock(double amount);

  /**
   * Retrieves the resize speed of this border.
   *
   * @return The border resize speed.
   */
  double getResizeSpeed();

  /**
   * Retrieves the warning distance of this border.
   *
   * @return The warning distance.
   */
  int getWarningDistance();

  /**
   * Changes the warning distance of this border.
   *
   * @param warningDistance The new warning distance.
   */
  void setWarningDistance(int warningDistance);
}
