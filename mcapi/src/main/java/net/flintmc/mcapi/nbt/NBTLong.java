package net.flintmc.mcapi.nbt;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/** A singed integral type. */
public interface NBTLong extends NBT {

  /**
   * Retrieves the long of the named binary tag.
   *
   * @return The long of the named binary tag.
   */
  long asLong();

  /** A factory class for the {@link NBTLong}. */
  @AssistedFactory(NBTLong.class)
  interface Factory {

    /**
     * Creates a new {@link NBTLong} with the given value.
     *
     * @param value The value for the named binary tag.
     * @return A created long named binary tag.
     */
    NBTLong create(@Assisted("value") long value);
  }
}
