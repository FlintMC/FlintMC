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
import net.flintmc.mcapi.world.math.Vector3I;

/** Default implementation of {@link Vector3I}. */
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

  /** {@inheritDoc} */
  @Override
  public int getX() {
    return this.x;
  }

  /** {@inheritDoc} */
  @Override
  public int getY() {
    return this.y;
  }

  /** {@inheritDoc} */
  @Override
  public int getZ() {
    return this.z;
  }

  /** {@inheritDoc} */
  @Override
  public Vector3I crossProduct(Vector3I vector) {
    return this.vector3IFactory.create(
        this.getY() * vector.getZ() - this.getZ() * vector.getY(),
        this.getZ() * vector.getX() - this.getX() * vector.getZ(),
        this.getX() * vector.getY() - this.getY() * vector.getX());
  }

  /** {@inheritDoc} */
  @Override
  public double distanceSq(double x, double y, double z, boolean useCenter) {
    double center = useCenter ? 0.5D : 0.0D;
    double distanceX = (double) this.getX() + center - x;
    double distanceY = (double) this.getY() + center - y;
    double distanceZ = (double) this.getZ() + center - z;
    return distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
  }

  /** {@inheritDoc} */
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
