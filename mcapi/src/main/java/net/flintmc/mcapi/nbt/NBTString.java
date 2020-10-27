package net.flintmc.mcapi.nbt;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/** A `UTF-8` string, It has a size, rather than being {@code null} terminated. */
public interface NBTString extends NBT {

  /** A factory class for the {@link NBTString}. */
  @AssistedFactory(NBTString.class)
  interface Factory {

    /**
     * Creates a new {@link NBTString} with the given value.
     *
     * @param value The value for the named binary tag.
     * @return A created string named binary tag.
     */
    NBTString create(@Assisted("value") String value);
  }
}
