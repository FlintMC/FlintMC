package net.flintmc.mcapi.nbt;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/** A singed floating point type. */
public interface NBTDouble extends NBT {

  /**
   * Retrieves the double of the named binary tag.
   *
   * @return The double of the named binary tag.
   */
  double asDouble();

  /** A factory class for the {@link NBTDouble}. */
  @AssistedFactory(NBTDouble.class)
  interface Factory {

    /**
     * Creates a new {@link NBTDouble} with the given value.
     *
     * @param value The value for the named binary tag.
     * @return A created double named binary tag.
     */
    NBTDouble create(@Assisted("value") double value);
  }
}
