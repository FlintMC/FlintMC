package net.flintmc.mcapi.render.image.util;

import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;

/** Renderer for item stacks from Minecraft. */
public interface ItemRenderer {

  /**
   * Draws the given item stack with its amount on the screen. The display name, lore, ... won't be
   * rendered when hovering over the item. This may create a copy of the item to get the Item for
   * Minecraft, for better performance you should use {@link #drawRawItemStack(float, float, float,
   * Object)}.
   *
   * <p>The item will be rendered in 16x16 pixel and scaled if set.
   *
   * @param x The x coordinate on the screen
   * @param y The y coordinate on the screen
   * @param scale The scale at which to scale the rendered item, 1 to disable scaling
   * @param item The non-null item to be rendered
   * @see #drawRawItemStack(float, float, float, Object)
   */
  void drawItemStack(float x, float y, float scale, ItemStack item);

  /**
   * Draws the given item stack with its amount on the screen. The display name, lore, ... won't be
   * rendered when hovering over the item. To create the item, {@link
   * MinecraftItemMapper#toMinecraft(ItemStack)} should be used only once and not every render tick.
   *
   * <p>The item will be rendered in 16x16 pixel and scaled if set.
   *
   * @param x The x coordinate on the screen
   * @param y The y coordinate on the screen
   * @param scale The scale at which to scale the rendered item, 1 to disable scaling
   * @param minecraftItem The non-null minecraft item to be rendered
   * @throws IllegalArgumentException If {@code minecraftItem} is not an instance of the Minecraft
   *     ItemStack
   * @see MinecraftItemMapper#toMinecraft(ItemStack)
   * @see #drawItemStack(float, float, float, ItemStack)
   */
  void drawRawItemStack(float x, float y, float scale, Object minecraftItem);
}
