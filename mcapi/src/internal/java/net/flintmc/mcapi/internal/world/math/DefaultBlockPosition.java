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
import net.flintmc.mcapi.world.math.BlockPosition;
import net.flintmc.mcapi.world.math.Vector3I;

/** Default implementation of {@link BlockPosition}. */
@Implement(BlockPosition.class)
public class DefaultBlockPosition extends DefaultVector3I implements BlockPosition {

  @AssistedInject
  private DefaultBlockPosition(
      @Assisted("x") int x,
      @Assisted("y") int y,
      @Assisted("z") int z,
      Vector3I.Factory vector3IFactory) {
    super(x, y, z, vector3IFactory);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (!(object instanceof BlockPosition)) {
      return false;
    } else {
      BlockPosition position = (BlockPosition) object;
      return position.getX() == this.getX()
          && position.getY() == this.getY()
          && position.getZ() == this.getZ();
    }
  }
}
