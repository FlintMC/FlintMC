package net.labyfy.component.gui.event;

/**
 * Event indicating that the window size has changed.
 */
public class WindowSizeEvent implements GuiEvent {
  private final int width;
  private final int height;

  /**
   * Constructs a new {@link WindowSizeEvent} with the specified with and height.
   *
   * @param width The new width of the window
   * @param height The new height of the window
   */
  public WindowSizeEvent(int width, int height) {
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
