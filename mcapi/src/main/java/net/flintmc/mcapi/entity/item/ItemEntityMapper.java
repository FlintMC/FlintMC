package net.flintmc.mcapi.entity.item;

/** Mapper between the Minecraft item entities and Labyfy entities. */
public interface ItemEntityMapper {

  /**
   * Creates a new {@link ItemEntity} by using the given Minecraft item entity as the base.
   *
   * @param handle The non-null Minecraft item entity.
   * @return The new Labyfy {@link ItemEntity} or {@code null} if the given item entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft item entity.
   */
  ItemEntity fromMinecraftItemEntity(Object handle);

  /**
   * Creates a new Minecraft item entity by using the Labyfy {@link ItemEntity} as the base.
   *
   * @param itemEntity The non-null Labyfy {@link ItemEntity}.
   * @return The new Minecraft item entity or {@code null} if the given item entity was invalid.
   */
  Object toMinecraftItemEntity(ItemEntity itemEntity);
}
