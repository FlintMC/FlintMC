package net.flintmc.render.gui.event;

import net.flintmc.render.gui.windowing.Window;

/** Event indicating that the cursor position has changed within the window bounds. */
public class CursorPosChangedEvent extends DefaultGuiEvent implements GuiEvent {
  private final double x;
  private final double y;

  /**
   * Constructs a new {@link CursorPosChangedEvent} with the given x and y coordinates.
   *
   * @param window The non-null window where this event has happened
   * @param x The new x coordinate of the mouse relative to the windows upper left corner
   * @param y The new y coordinate of the mouse relative to the windows upper left corner
   */
  public CursorPosChangedEvent(Window window, double x, double y) {
    super(window);
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
