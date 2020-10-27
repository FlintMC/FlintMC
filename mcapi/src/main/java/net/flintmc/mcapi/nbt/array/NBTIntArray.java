package net.flintmc.mcapi.nbt.array;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.nbt.NBT;

/** An array of int's payloads. */
public interface NBTIntArray extends NBT {

  /**
   * Retrieves the array of the named binary tag.
   *
   * @return The array of the named binary tag.
   */
  int[] asArray();

  /** A factory class for the {@link NBTIntArray}. */
  @AssistedFactory(NBTIntArray.class)
  interface Factory {

    /**
     * Creates a new {@link NBTIntArray} with the given array.
     *
     * @param value The array for the named binary tag.
     * @return A created int array named binary tag.
     */
    NBTIntArray create(@Assisted("value") int[] value);
  }
}
