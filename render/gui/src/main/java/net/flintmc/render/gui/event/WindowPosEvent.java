package net.flintmc.render.gui.event;

import net.flintmc.render.gui.windowing.Window;

/** Event indicating that the window position has changed. */
public class WindowPosEvent extends DefaultGuiEvent implements GuiEvent {
  private final int x;
  private final int y;

  /**
   * Constructs a new {@link WindowPosEvent} with the specified x and y coordinates.
   *
   * @param window The non-null window where this event has happened
   * @param x The new x coordinate of the window
   * @param y The new y coordinate of the window
   */
  public WindowPosEvent(Window window, int x, int y) {
    super(window);
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
