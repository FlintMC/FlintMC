package net.flintmc.mcapi.player.type.model;

/** Mapper between the Minecraft player model part and the Labyfy {@link PlayerClothing}. */
public interface ModelMapper {

  /**
   * Retrieves a {@link PlayerClothing} constant by using the given Minecraft player model part.
   *
   * @param playerModelPart The non-null minecraft player model part.
   * @return The {@link PlayerClothing} constant.
   * @throws IllegalArgumentException If the given object is not a Minecraft player model part.
   */
  PlayerClothing fromMinecraftPlayerModelPart(Object playerModelPart);

  /**
   * Retrieves a Minecraft player model part constant by using the given {@link PlayerClothing}.
   *
   * @param playerClothing The non-null player clothing.
   * @return The player model part constant.
   */
  Object toMinecraftPlayerModelPart(PlayerClothing playerClothing);
}
