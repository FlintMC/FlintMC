package net.flintmc.mcapi.internal.world.math;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.math.Vector3I;

/**
 * Default implementation of {@link Vector3I}.
 */
@Implement(Vector3I.class)
public class DefaultVector3I implements Vector3I {

  private final int x;
  private final int y;
  private final int z;
  private final Vector3I.Factory vector3IFactory;

  @AssistedInject
  protected DefaultVector3I(
      @Assisted("x") int x,
      @Assisted("y") int y,
      @Assisted("z") int z,
      Vector3I.Factory vector3IFactory) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.vector3IFactory = vector3IFactory;
  }

  @AssistedInject
  protected DefaultVector3I(@Assisted("vector") Vector3I vector, Vector3I.Factory vector3IFactory) {
    this(vector.getX(), vector.getY(), vector.getZ(), vector3IFactory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getX() {
    return this.x;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getY() {
    return this.y;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getZ() {
    return this.z;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Vector3I crossProduct(Vector3I vector) {
    return this.vector3IFactory.create(
        this.getY() * vector.getZ() - this.getZ() * vector.getY(),
        this.getZ() * vector.getX() - this.getX() * vector.getZ(),
        this.getX() * vector.getY() - this.getY() * vector.getX());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int distanceSq(int x, int y, int z) {
    int distanceX = this.getX() - x;
    int distanceY = this.getY() - y;
    int distanceZ = this.getZ() - z;
    return distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int manhattanDistance(Vector3I vector) {
    int absX = vector.getX() - this.getX();
    int absY = vector.getY() - this.getY();
    int absZ = vector.getZ() - this.getZ();
    return absX + absY + absZ;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (!(object instanceof Vector3I)) {
      return false;
    } else {
      Vector3I vector = (Vector3I) object;

      return this.getX() == vector.getX()
          && this.getY() == vector.getY()
          && this.getZ() == vector.getZ();
    }
  }

  @Override
  public int hashCode() {
    return (this.getY() + this.getZ() * 31) * 31 + this.getX();
  }
}
