package net.labyfy.component.gui.event;

public class MouseButton implements GuiInputEvent {
  public static final int LEFT = 0;
  public static final int RIGHT = 1;
  public static final int MIDDLE = 2;

  private final State state;
  private final int value;

  public MouseButton(State state, int value) {
    this.state = state;
    this.value = value;
  }

  public State getState() {
    return state;
  }

  public int getValue() {
    return value;
  }

  public enum State {
    PRESS,
    RELEASE,
    REPEAT
  }
}
