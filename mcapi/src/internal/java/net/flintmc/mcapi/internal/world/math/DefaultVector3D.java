/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.internal.world.math;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.math.Vector3D;
import net.flintmc.mcapi.world.math.Vector3I;

/** Default implementation of {@link Vector3I}. */
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
      Vector3D.Factory vector3DFactory) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.vector3DFactory = vector3DFactory;
  }

  @AssistedInject
  protected DefaultVector3D(@Assisted("vector") Vector3D vector, Vector3D.Factory vector3DFactory) {
    this(vector.getX(), vector.getY(), vector.getZ(), vector3DFactory);
  }

  /** {@inheritDoc} */
  @Override
  public double getX() {
    return this.x;
  }

  /** {@inheritDoc} */
  @Override
  public double getY() {
    return this.y;
  }

  /** {@inheritDoc} */
  @Override
  public double getZ() {
    return this.z;
  }

  /** {@inheritDoc} */
  @Override
  public Vector3D crossProduct(Vector3D vector) {
    return this.vector3DFactory.create(
        this.getY() * vector.getZ() - this.getZ() * vector.getY(),
        this.getZ() * vector.getX() - this.getX() * vector.getZ(),
        this.getX() * vector.getY() - this.getY() * vector.getX());
  }

  /** {@inheritDoc} */
  @Override
  public double distanceSq(double x, double y, double z, boolean useCenter) {
    double center = useCenter ? 0.5D : 0.0D;
    double distanceX = this.getX() + center - x;
    double distanceY = this.getX() + center - y;
    double distanceZ = this.getX() + center - z;
    return distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
  }

  /** {@inheritDoc} */
  @Override
  public int manhattanDistance(Vector3D vector) {
    float absX = (float) Math.abs(vector.getX() - this.getX());
    float absY = (float) Math.abs(vector.getY() - this.getY());
    float absZ = (float) Math.abs(vector.getZ() - this.getZ());
    return (int) (absX + absY + absZ);
  }

  /** {@inheritDoc} */
  @Override
  public Vector3D multiply(Vector3D vector) {
    return this.multiply(vector.getX(), vector.getY(), vector.getZ());
  }

  /** {@inheritDoc} */
  @Override
  public Vector3D multiply(double factorX, double factorY, double factorZ) {
    return this.vector3DFactory.create(
        this.getX() * factorX, this.getY() * factorY, this.getZ() * factorZ);
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
