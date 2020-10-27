package net.flintmc.mcapi.nbt.array;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.nbt.NBT;

/** An array of bytes. */
public interface NBTByteArray extends NBT {

  /**
   * Retrieves the array of the named binary tag.
   *
   * @return The array of the named binary tag.
   */
  byte[] asArray();

  /** A factory for {@link NBTByteArray}. */
  @AssistedFactory(NBTByteArray.class)
  interface Factory {

    /**
     * Creates a new {@link NBTByteArray} with the given array.
     *
     * @param value The array for the named binary tag.
     * @return A created byt array named binary tag.
     */
    NBTByteArray create(@Assisted("value") byte[] value);
  }
}
