package net.flintmc.mcapi.items.mapper;

import net.flintmc.mcapi.items.ItemRegistry;
import net.flintmc.mcapi.items.ItemStack;

/**
 * A mapper between the Flint {@link ItemStack} and the Minecraft ItemStack.
 */
public interface MinecraftItemMapper {

  /**
   * Maps the given Minecraft ItemStack to a Flint {@link ItemStack}.
   *
   * @param handle The non-null minecraft ItemStack
   * @return The new non-null Flint ItemStack
   * @throws ItemMappingException If the given object is not an instance of the minecraft ItemStack
   * @throws ItemMappingException If no item matching the given stack exists in the {@link
   *     ItemRegistry}.
   */
  ItemStack fromMinecraft(Object handle) throws ItemMappingException;

  /**
   * Maps the given Flint {@link ItemStack} to a minecraft ItemStack.
   *
   * @param stack The non-null Flint {@link ItemStack}
   * @return The new non-null minecraft ItemStack
   * @throws ItemMappingException If no item matching the given stack exists in the Item registry in
   *     minecraft.
   */
  Object toMinecraft(ItemStack stack) throws ItemMappingException;
}
