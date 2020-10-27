package net.flintmc.mcapi.nbt.mapper;

import net.flintmc.mcapi.nbt.NBT;

/**
 * Mapper between the Minecraft NBT and Labyfy NBT.
 */
public interface NBTMapper {

  /**
   * Creates a new {@link NBT} by using the given Minecraft nbt as the base.
   *
   * @param handle The non-null minecraft nbt.
   * @return The new Labyfy {@link NBT} or {@code null} if the given nbt was invalid.
   * @throws IllegalArgumentException IF the given object is no Minecraft nbt.
   */
  NBT fromMinecraftNBT(Object handle);

  /**
   * Creates a new Minecraft nbt by the given Labyfy {@link NBT} as the base.
   *
   * @param nbt The non-null Labyfy {@link NBT}.
   * @return The new Minecraft nbt or {@code null} if the given component was invalid.
   */
  Object toMinecraftNBT(NBT nbt);

}
