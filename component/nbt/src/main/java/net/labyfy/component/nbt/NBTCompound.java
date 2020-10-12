package net.labyfy.component.nbt;

import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.Map;

/**
 * A list of fully formed tags, including their ID's, names, and payloads. No two tags may have the same name.
 */
public interface NBTCompound extends NBT {

  /**
   * Retrieves the size of the key-value system.
   *
   * @return The key-value size.
   */
  int getSize();

  /**
   * Whether a tag exist inside this compound with the given key.
   *
   * @param key The key.
   * @return {@code true} if a tag exists inside this compound with the given key, otherwise {@code false}.
   */
  boolean containsKey(String key);

  /**
   * Retrieves the tag associated to the given key, if any.
   *
   * @param key The key.
   * @return The tag associated to the given key, if any or {@code null}.
   */
  NBT get(String key);

  /**
   * Sets tag associated to the given key.
   *
   * @param key The key to associated.
   * @param tag The tag for the key.
   * @return This compound.
   */
  NBTCompound set(String key, NBT tag);

  /**
   * Retrieves the key-value system of this compound.
   *
   * @return The key-value system.
   */
  Map<String, NBT> getTags();

  /**
   * A factory class for the {@link NBTCompound}.
   */
  @AssistedFactory(NBTCompound.class)
  interface Factory {

    /**
     * Creates a new {@link NBTCompound}.
     *
     * @return A created compound named binary tag.
     */
    NBTCompound create();

  }
}
