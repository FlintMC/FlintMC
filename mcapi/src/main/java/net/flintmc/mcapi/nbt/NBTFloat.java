package net.flintmc.mcapi.nbt;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/** A signed floating point type. */
public interface NBTFloat extends NBT {

  /**
   * Retrieves the float of the named binary tag.
   *
   * @return The float of the named binary tag.
   */
  float asFloat();

  /** A factory class for the {@link NBTFloat}. */
  @AssistedFactory(NBTFloat.class)
  interface Factory {

    /**
     * Creates a new {@link NBTFloat} with the given value.
     *
     * @param value The value for the named binary tag.
     * @return A created float named binary tag.
     */
    NBTFloat create(@Assisted("value") float value);
  }
}