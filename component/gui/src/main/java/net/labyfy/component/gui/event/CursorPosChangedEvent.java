package net.labyfy.component.gui.event;

/**
 * Event indicating that the cursor position has changed within the window bounds.
 */
public class CursorPosChangedEvent implements GuiInputEvent {
  private final double x;
  private final double y;

  /**
   * Constructs a new {@link CursorPosChangedEvent} with the given
   * x and y coordinates.
   *
   * @param x The new x coordinate of the mouse relative to the windows upper left corner
   * @param y The new y coordinate of the mouse relative to the windows upper left corner
   */
  public CursorPosChangedEvent(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Retrieves the new x coordinate of the mouse cursor
   *
   * @return The new x coordinate relative to the windows upper left corner
   */
  public double getX() {
    return x;
  }

  /**
   * Retrieves the new y coordinate of the mouse cursor
   *
   * @return The new y coordinate relative the windows upper left corner
   */
  public double getY() {
    return y;
  }
}
