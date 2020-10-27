package net.flintmc.mcapi.internal.world.math;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.math.Vector3D;
import net.flintmc.mcapi.world.math.Vector3I;

/**
 * Default implementation of {@link Vector3I}.
 */
@Implement(Vector3D.class)
public class DefaultVector3D implements Vector3D {

  private final double x;
  private final double y;
  private final double z;
  private final Vector3D.Factory vector3DFactory;

  @AssistedInject
  protected DefaultVector3D(
          @Assisted("x") double x,
          @Assisted("y") double y,
          @Assisted("z") double z,
          Vector3D.Factory vector3DFactory
  ) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.vector3DFactory = vector3DFactory;
  }

  @AssistedInject
  protected DefaultVector3D(
          @Assisted("vector") Vector3D vector,
          Vector3D.Factory vector3DFactory) {
    this(vector.getX(), vector.getY(), vector.getZ(), vector3DFactory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getX() {
    return this.x;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getY() {
    return this.y;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getZ() {
    return this.z;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Vector3D crossProduct(Vector3D vector) {
    return this.vector3DFactory.create(
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
    double distanceX = this.getX() + center - x;
    double distanceY = this.getX() + center - y;
    double distanceZ = this.getX() + center - z;
    return distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int manhattanDistance(Vector3D vector) {
    float absX = (float) Math.abs(vector.getX() - this.getX());
    float absY = (float) Math.abs(vector.getY() - this.getY());
    float absZ = (float) Math.abs(vector.getZ() - this.getZ());
    return (int) (absX + absY + absZ);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Vector3D multiply(Vector3D vector) {
    return this.multiply(vector.getX(), vector.getY(), vector.getZ());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Vector3D multiply(double factorX, double factorY, double factorZ) {
    return this.vector3DFactory.create(
            this.getX() * factorX,
            this.getY() * factorY,
            this.getZ() * factorZ
    );
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (!(object instanceof Vector3D)) {
      return false;
    } else {
      Vector3D vector = (Vector3D) object;

      if (this.getX() != vector.getX()) {
        return false;
      } else if (this.getY() != vector.getY()) {
        return false;
      } else {
        return this.getZ() == vector.getZ();
      }
    }
  }

  @Override
  public int hashCode() {
    return (int) ((this.getY() + this.getZ() * 31) * 31 + this.getX());
  }
}
