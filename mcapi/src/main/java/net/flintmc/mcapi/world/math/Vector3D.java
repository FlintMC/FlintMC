package net.flintmc.mcapi.world.math;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/** Represents a three-dimensional vector based on {@link Double}. */
public interface Vector3D {

  /**
   * Retrieves the X coordinate of this vector.
   *
   * @return The X coordinate.
   */
  double getX();

  /**
   * Retrieves the Y coordinate of this vector.
   *
   * @return The Y coordinate.
   */
  double getY();

  /**
   * Retrieves the Z coordinate of this vector.
   *
   * @return The Z coordinate.
   */
  double getZ();

  /**
   * Calculates the cross product of this and the given {@link Vector3D}
   *
   * @param vector The vector for the calculation.
   * @return The calculated cross product.
   */
  Vector3D crossProduct(Vector3D vector);

  /**
   * Whether the given distance is larger than the square vector.
   *
   * @param vector The vector to calculate the square distance.
   * @param distance The distance.
   * @return {@code true} if the distance larger than the square distance, otherwise {@code false}.
   */
  default boolean withinDistance(Vector3D vector, double distance) {
    return this.distanceSq(vector.getX(), vector.getY(), vector.getZ(), false)
        < distance * distance;
  }

  /**
   * The square of the distance from this {@link Vector3D} to a specified point.
   *
   * @param vector The vector of the specified point to be measured against this {@link Vector3D}.
   * @return The square of the distance from this {@link Vector3D} to a specified point.
   */
  default double distanceSq(Vector3D vector) {
    return this.distanceSq(vector, false);
  }

  /**
   * The square of the distance from this {@link Vector3D} to a specified point.
   *
   * @param vector The vector of the specified point to be measured against this {@link Vector3D}.
   * @param useCenter {@code true} if the center should be used, otherwise {@code false}.
   * @return The square of the distance from this {@link Vector3D} to a specified point.
   */
  default double distanceSq(Vector3D vector, boolean useCenter) {
    return this.distanceSq(vector.getX(), vector.getY(), vector.getZ(), useCenter);
  }

  /**
   * The square of the distance from this {@link Vector3D} to a specified point.
   *
   * @param x The X coordinate of the specified point to be measured against this {@link Vector3D}.
   * @param y The Y coordinate of the specified point to be measured against this {@link Vector3D}.
   * @param z The Z coordinate of the specified point to be measured against this {@link Vector3D}.
   * @return The square of the distance from this {@link Vector3D} to a specified point.
   */
  default double distanceSq(double x, double y, double z) {
    return this.distanceSq(x, y, z, false);
  }

  /**
   * The square of the distance from this {@link Vector3D} to a specified point.
   *
   * @param x The X coordinate of the specified point to be measured against this {@link Vector3D}.
   * @param y The Y coordinate of the specified point to be measured against this {@link Vector3D}.
   * @param z The Z coordinate of the specified point to be measured against this {@link Vector3D}.
   * @param useCenter {@code true} if the center should be used, otherwise {@code false}.
   * @return The square of the distance from this {@link Vector3D} to a specified point.
   */
  double distanceSq(double x, double y, double z, boolean useCenter);

  /**
   * Calculates the distance between three points.
   *
   * @param vector The vector for the calculation.
   * @return The calculated distance between three points.
   */
  int manhattanDistance(Vector3D vector);

  /**
   * Multiplies the current vector with the given vector.
   *
   * @param vector The vector to multiply.
   * @return A multiplied vector.
   */
  Vector3D multiply(Vector3D vector);

  /**
   * Multiplies the current vector with the given parameters.
   *
   * @param factorX The x factor.
   * @param factorY The y factor.
   * @param factorZ The z factor.
   * @return A multiplied vector.
   */
  Vector3D multiply(double factorX, double factorY, double factorZ);

  /** A factory class for the {@link Vector3D}. */
  @AssistedFactory(Vector3D.class)
  interface Factory {

    /**
     * Creates a new {@link Vector3D} with the given parameters.
     *
     * @param x The x position of the vector.
     * @param y The y position of the vector.
     * @param z The z position of the vector.
     * @return A created vector.
     */
    Vector3D create(@Assisted("x") double x, @Assisted("y") double y, @Assisted("z") double z);

    /**
     * Creates a new {@link Vector3D} with the given vector.
     *
     * @param vector The vector.
     * @return A created vector.
     */
    Vector3D create(@Assisted("vector") Vector3D vector);
  }
}
