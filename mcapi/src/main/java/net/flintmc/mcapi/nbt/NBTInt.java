package net.flintmc.mcapi.nbt;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/** A singed integral type. */
public interface NBTInt extends NBT {

  /**
   * Retrieves the integer of the named binary tag.
   *
   * @return The integer of the named binary tag.
   */
  int asInt();

  /** A factory class for the {@link NBTInt}. */
  @AssistedFactory(NBTInt.class)
  interface Factory {

    /**
     * Creates a new {@link NBTInt} with the given value.
     *
     * @param value The value for the named binary tag.
     * @return A created integer named binary tag.
     */
    NBTInt create(@Assisted("value") int value);
  }
}
