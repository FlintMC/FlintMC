package net.flintmc.render.gui.event;

/**
 * Event indicating that the user has done scroll input
 */
public class MouseScrolledEvent implements GuiEvent {
  private final double xOffset;
  private final double yOffset;

  /**
   * Constructs a new {@link MouseScrolledEvent} with the given offsets
   *
   * @param xOffset The amount the user has scrolled on the x axis
   * @param yOffset The amount the user has scrolled on the y axis
   */
  public MouseScrolledEvent(double xOffset, double yOffset) {
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
