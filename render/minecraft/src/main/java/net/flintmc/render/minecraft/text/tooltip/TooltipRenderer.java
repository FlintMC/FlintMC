package net.flintmc.render.minecraft.text.tooltip;

/**
 * Renderer for the tooltips that for example will be shown when the user hovers over an item in
 * their Inventory.
 */
public interface TooltipRenderer {

  /**
   * Renders the given text with the background as a tooltip. {@link TooltipRenderBuilder} should be
   * used instead of directly accessing this method for easier usage. The rendered tooltip cannot be
   * wider than 200 or half of the screen width, depending on what's the larger one.
   *
   * <p>Note that the position is not exactly a corner of the tooltip, Minecraft will automatically
   * position the tooltip so that it fits on the screen with this coordinate being just a hint on
   * where it should be rendered.
   *
   * @param x The x position on the screen where the tooltip should be rendered
   * @param y The y position on the screen where the tooltip should be rendered
   * @param text The non-null text to be rendered
   */
  void renderTooltip(float x, float y, String text);
}
