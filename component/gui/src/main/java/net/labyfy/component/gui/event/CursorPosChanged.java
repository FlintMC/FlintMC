package net.labyfy.component.gui.event;

public class CursorPosChanged implements GuiInputEvent {
  private final double x;
  private final double y;

  public CursorPosChanged(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }
}
