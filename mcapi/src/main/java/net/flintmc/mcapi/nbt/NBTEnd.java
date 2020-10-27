package net.flintmc.mcapi.nbt;

import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Used to mark the end of compound tags. This tag does not have a name, so it is only ever a single byte {@code 0}.
 * It may also be the type of empty List tags.
 */
public interface NBTEnd extends NBT {

  /**
   * A factory class for the {@link NBTEnd}.
   */
  @AssistedFactory(NBTEnd.class)
  interface Factory {

    /**
     * Creates a new {@link NBTEnd}.
     *
     * @return A created end named binary tag.
     */
    NBTEnd create();

  }
}
