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
