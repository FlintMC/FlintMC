package net.flintmc.mcapi.nbt;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * A singed integral type. Sometimes used for booleans.
 */
public interface NBTByte extends NBT {

  /**
   * Retrieves the byte of the named binary tag.
   *
   * @return The byte of the named binary tag.
   */
  byte asByte();

  /**
   * A factory class for the {@link NBTByte}.
   */
  @AssistedFactory(NBTByte.class)
  interface Factory {

    /**
     * Creates a new {@link NBTByte} with the given value.
     *
     * @param value The value for the named binary tag.
     * @return A created byte named binary tag.
     */
    NBTByte create(@Assisted("value") byte value);
  }
}
