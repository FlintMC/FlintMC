package net.labyfy.internal.component.world.util;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.world.util.BlockPosition;
import net.labyfy.component.world.util.Vector3I;

/**
 * Default implementation of {@link BlockPosition}.
 */
@Implement(BlockPosition.class)
public class DefaultBlockPosition extends DefaultVector3I implements BlockPosition {

  @AssistedInject
  private DefaultBlockPosition(
          @Assisted("x") int x,
          @Assisted("y") int y,
          @Assisted("z") int z
  ) {
    super(x, y, z);
  }
}
