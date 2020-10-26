package net.labyfy.internal.component.world.math;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.world.math.Vector3I;

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
          Vector3I.Factory vector3IFactory
  ) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.vector3IFactory = vector3IFactory;
  }

  @AssistedInject
  protected DefaultVector3I(
          @Assisted("vector") Vector3I vector,
          Vector3I.Factory vector3IFactory) {
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
            this.getX() * vector.getY() - this.getY() * vector.getX()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double distanceSq(double x, double y, double z, boolean useCenter) {
    double center = useCenter ? 0.5D : 0.0D;
    double distanceX = (double) this.getX() + center - x;
    double distanceY = (double) this.getX() + center - y;
    double distanceZ = (double) this.getX() + center - z;
    return distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int manhattanDistance(Vector3I vector) {
    float absX = (float) Math.abs(vector.getX() - this.getX());
    float absY = (float) Math.abs(vector.getY() - this.getY());
    float absZ = (float) Math.abs(vector.getZ() - this.getZ());
    return (int) (absX + absY + absZ);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (!(object instanceof Vector3I)) {
      return false;
    } else {
      Vector3I vector = (Vector3I) object;

      return this.getX() == vector.getX() && this.getY() == vector.getY() && this.getZ() == vector.getZ();
    }
  }

  @Override
  public int hashCode() {
    return (this.getY() + this.getZ() * 31) * 31 + this.getX();
  }
}
