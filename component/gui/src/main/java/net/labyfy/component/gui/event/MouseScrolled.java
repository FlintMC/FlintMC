package net.labyfy.component.gui.event;

public class MouseScrolled implements GuiInputEvent {
  private final double xOffset;
  private final double yOffset;

  public MouseScrolled(double xOffset, double yOffset) {
    this.xOffset = xOffset;
    this.yOffset = yOffset;
  }

  public double getXOffset() {
    return xOffset;
  }

  public double getYOffset() {
    return yOffset;
  }
}
