package net.flintmc.render.gui.event;

import net.flintmc.render.gui.windowing.Window;

/** Event indicating that the framebuffer size has changed. */
public class FramebufferSizeEvent extends DefaultGuiEvent implements GuiEvent {
  private final int width;
  private final int height;

  /**
   * Constructs a new {@link FramebufferSizeEvent} with the specified with and height.
   *
   * @param window The non-null window where this event has happened
   * @param width The new width of the framebuffer in pixels
   * @param height The new height of the framebuffer in pixels
   */
  public FramebufferSizeEvent(Window window, int width, int height) {
    super(window);
    this.width = width;
    this.height = height;
  }

  /**
   * Retrieves the new width of the framebuffer in pixels.
   *
   * @return The new width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Retrieves the new height of the framebuffer in pixels.
   *
   * @return The new height
   */
  public int getHeight() {
    return height;
  }
}
