package net.flintmc.render.gui.event;

import net.flintmc.render.gui.windowing.Window;

/** Event indicating that the user has done scroll input */
public class MouseScrolledEvent extends DefaultGuiEvent implements GuiEvent {
  private final double xOffset;
  private final double yOffset;

  /**
   * Constructs a new {@link MouseScrolledEvent} with the given offsets
   *
   * @param window The non-null window where this event has happened
   * @param xOffset The amount the user has scrolled on the x axis
   * @param yOffset The amount the user has scrolled on the y axis
   */
  public MouseScrolledEvent(Window window, double xOffset, double yOffset) {
    super(window);
    this.xOffset = xOffset;
    this.yOffset = yOffset;
  }

  /**
   * Retrieves the amount the user has scrolled on the x axis
   *
   * @return Amount the user has scrolled horizontally
   */
  public double getXOffset() {
    return xOffset;
  }

  /**
   * Retrieves the amount the user has scrolled on the y axis
   *
   * @return Amount the user has scrolled vertically
   */
  public double getYOffset() {
    return yOffset;
  }
}
