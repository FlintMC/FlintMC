package net.flintmc.mcapi.render.image;

import net.flintmc.mcapi.resources.ResourceLocation;

/**
 * Singleton builder for rendering a full image from a resource. This builder is not thread-safe and
 * may only be used on the render thread of Minecraft.
 *
 * <p>The necessary values to be set are the following:
 *
 * <ul>
 *   <li>{@link #at(float, float)}
 *   <li>{@link #fullImageSize(float, float)}
 * </ul>
 *
 * @see ImageRenderer#drawFullImage(float, float, int, Object, float, float, float, float, int, int,
 * int, int)
 */
public interface ImageFullRenderBuilder {

  /**
   * Defines the position (top-left corner) where the image should be rendered.
   *
   * @param x The x position of the top-left corner
   * @param y The y position of the top-left corner
   * @return this builder for chaining
   */
  ImageFullRenderBuilder at(float x, float y);

  /**
   * Defines the matrix to be used for rendering of the image, this is version specific. By default,
   * no matrix will be used.
   *
   * @param matrix The matrix to be used, null to use no matrix
   * @return this builder for chaining
   */
  ImageFullRenderBuilder matrix(Object matrix);

  /**
   * Changes the size of the image in the resource location. If {@link #displaySize(float, float)}
   * is not set, this will also be the size as it will be displayed on the screen without any
   * scaling.
   *
   * @param width  The width of the image in the resource location in pixels
   * @param height The height of the image in the resource location in pixels
   * @return this builder for chaining
   */
  ImageFullRenderBuilder fullImageSize(float width, float height);

  /**
   * Changes the size of the image as it will be displayed on the screen. If this is not set, {@link
   * #fullImageSize(float, float)} will be used as the size to display the image on the screen.
   *
   * @param width  The width of the image on the screen in pixels
   * @param height The height of the image on the screen in pixels
   * @return this builder for chaining
   */
  ImageFullRenderBuilder displaySize(float width, float height);

  /**
   * Changes the color of the image. For example if the image is white, these colors will be
   * modified with the given color
   *
   * @param rgba The RGBA value of the new color
   * @return this builder for chaining
   */
  ImageFullRenderBuilder color(int rgba);

  /**
   * Changes the color of the image. For example if the image is white, these colors will be
   * modified with the given color
   *
   * @param r The red component of the color from 0 - 255
   * @param g The green component of the color from 0 - 255
   * @param b The blue component of the color from 0 - 255
   * @return this builder for chaining
   */
  ImageFullRenderBuilder color(int r, int g, int b);

  /**
   * Changes the color of the image. For example if the image is white, these colors will be
   * modified with the given color
   *
   * @param r The red component of the color from 0 - 255
   * @param g The green component of the color from 0 - 255
   * @param b The blue component of the color from 0 - 255
   * @param a The alpha component of the color from 0 - 255
   * @return this builder for chaining
   */
  ImageFullRenderBuilder color(int r, int g, int b, int a);

  /**
   * Changes the z level on which the image should be rendered.
   *
   * @param zLevel The z level which can be any number
   * @return this builder for chaining
   */
  ImageFullRenderBuilder zLevel(int zLevel);

  /**
   * Draws the values that have been set in this builder on the screen and resets this builder to be
   * re-used for the next rendering.
   *
   * <p><b>Important</b>: The texture to be drawn needs to be bound first with {@link
   * ImageRenderer#bindTexture(ResourceLocation)}
   *
   * @throws IllegalArgumentException If no fullImageHeight (or something <= 0) and/or color
   *                                  components (r, g, b, a) not >= 0 and <= 255
   * @see ImageRenderer#drawFullImage(float, float, int, Object, float, float, float, float, int,
   * int, int, int)
   */
  void draw() throws IllegalArgumentException;
}
