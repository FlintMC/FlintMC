package net.labyfy.component.gui.event;

/**
 * Event indicating that the window position has changed.
 */
public class WindowPosEvent implements GuiEvent {
  private final int x;
  private final int y;

  /**
   * Constructs a new {@link WindowPosEvent} with the specified x and y coordinates.
   *
   * @param x The new x coordinate of the window
   * @param y The new y coordinate of the window
   */
  public WindowPosEvent(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Retrieves the new x coordinate of the window.
   *
   * @return The new x coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Retrieves the new y coordinate of the window.
   *
   * @return The new y coordinate
   */
  public int getY() {
    return y;
  }
}
