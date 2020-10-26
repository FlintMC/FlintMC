package net.labyfy.component.nbt.serializer;

import com.google.gson.JsonElement;
import net.labyfy.component.nbt.NBT;

/**
 * A serializer which serializes a {@link NBT} to a {@link JsonElement}
 * or deserializes a {@link JsonElement} to a {@link NBT}.
 */
public interface NBTSerializer {

  /**
   * Serializes the given {@link NBT} to a {@link JsonElement}.
   *
   * @param nbt The named binary tag to serialize.
   * @return The serialized nbt as a {@link JsonElement}.
   * @throws AssertionError                If thrown when a terminated named binary tag comes,
   *                                       should not be encountered.
   * @throws UnsupportedOperationException If thrown when a new NBT class is made.
   */
  JsonElement serialize(NBT nbt);

  /**
   * Deserializes the given {@link JsonElement} to a {@link NBT}.
   *
   * @param element The json to deserialize.
   * @return The deserialized json as a {@link NBT}.
   * @throws AssertionError Is thrown when something goes wrong.
   */
  NBT deserialize(JsonElement element);

}
