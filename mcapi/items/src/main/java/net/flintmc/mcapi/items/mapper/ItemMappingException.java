package net.flintmc.mcapi.items.mapper;

/**
 * Thrown by the {@link MinecraftItemMapper} when an error occurred while mapping the minecraft ItemStack to the Labyfy
 * ItemStack and the other way around.
 */
public class ItemMappingException extends RuntimeException {

  public ItemMappingException(String message) {
    super(message);
  }

  public ItemMappingException(String message, Throwable cause) {
    super(message, cause);
  }

}
