package net.flintmc.render.gui.event;

import net.flintmc.render.gui.windowing.Window;

/** Event indicating that the window size has changed. */
public class WindowSizeEvent extends DefaultGuiEvent implements GuiEvent {
  private final int width;
  private final int height;

  /**
   * Constructs a new {@link WindowSizeEvent} with the specified with and height.
   *
   * @param window The non-null window where this event has happened
   * @param width The new width of the window
   * @param height The new height of the window
   */
  public WindowSizeEvent(Window window, int width, int height) {
    super(window);
    this.width = width;
    this.height = height;
  }

  /**
   * Retrieves the new width of the window.
   *
   * @return The new width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Retrieves the new height of the window
   *
   * @return The new height
   */
  public int getHeight() {
    return height;
  }
}
