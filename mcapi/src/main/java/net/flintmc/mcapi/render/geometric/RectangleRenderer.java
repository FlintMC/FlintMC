package net.flintmc.mcapi.render.geometric;

/** Renderer for simple rectangles. */
public interface RectangleRenderer {

  /**
   * Renders the border of a rectangle with the given borderWidth, the width will be added to the
   * inside of the rectangle.
   *
   * @param x The x position of the top-left corner, the border will be drawn to the bottom-right
   *     from this position
   * @param y The y position of the top-left corner, the border will be drawn to the bottom-right
   *     from this position
   * @param width The width of the rectangle, x + width will be the most right position of the
   *     border
   * @param height The width of the rectangle, y + height will be the lowest position of the border
   * @param rgba The RGBA value to draw the border in
   * @param borderWidth The width of the border, should be > 0
   */
  void drawRectBorder(float x, float y, float width, float height, int rgba, float borderWidth);

  /**
   * Renders the full rectangle.
   *
   * @param x The x position of the top-left corner
   * @param y The y position of the top-left corner
   * @param width The width of the rectangle, x + width will be the most right position of the
   *     rectangle
   * @param height The width of the rectangle, y + height will be the lowest position of the
   *     rectangle
   * @param rgba The RGBA value to draw the rectangle in
   */
  void drawFilledRect(float x, float y, float width, float height, int rgba);
}
