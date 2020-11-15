package net.flintmc.render.minecraft.image;

import net.flintmc.mcapi.resources.ResourceLocation;

/**
 * Singleton builder for rendering parts of an image in a resource. This builder is not thread-safe
 * and may only be used on the render thread of Minecraft.
 *
 * <p>The necessary values to be set are the following:
 *
 * <ul>
 *   <li>{@link #at(float, float)}
 *   <li>{@link #fullImageSize(float, float)}
 *   <li>{@link #sourcePosition(float, float)}
 *   <li>{@link #sourceSize(float, float)}
 * </ul>
 *
 * @see ImageRenderer#drawPartImage(float, float, float, float, int, float, float, float, float,
 *     float, float, int, int, int, int)
 */
public interface ImagePartRenderBuilder extends ImageFullRenderBuilder {

  /**
   * Changes the position of the content in the texture to be drawn on the screen.
   *
   * @param x The x position of one pixel above the top-left corner of the content in the texture to
   *     draw on the screen
   * @param y The y position of one pixel above the top-left corner of the content in the texture to
   *     draw on the screen
   * @return this builder for chaining
   */
  ImagePartRenderBuilder sourcePosition(float x, float y);

  /**
   * Changes the size of the content in the texture to be drawn on the screen.
   *
   * @param width The width of the content in the texture to be drawn
   * @param height The height of the content in the texture to be drawn
   * @return this builder for chaining
   */
  ImagePartRenderBuilder sourceSize(float width, float height);

  /**
   * Draws the values that have been set in this builder on the screen and resets this builder to be
   * re-used for the next rendering.
   *
   * <p><b>Important</b>: The texture to be drawn needs to be bound first with {@link
   * ImageRenderer#bindTexture(ResourceLocation)}
   *
   * @throws IllegalArgumentException If no fullImageHeight (or something <= 0), no position (or
   *     something < 0), no source position (or something < 0), no source size (or something <= 0)
   *     and/or color components (r, g, b, a) not >= 0 and <= 255
   * @see ImageRenderer#drawPartImage(float, float, float, float, int, float, float, float, float,
   *     float, float, int, int, int, int)
   */
  void draw() throws IllegalArgumentException;
}
