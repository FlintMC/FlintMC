package net.labyfy.component.gui.event;

/**
 * Event indicating that a mouse button state has changed or is still being hold.
 */
public class MouseButtonEvent implements GuiEvent {
  public static final int LEFT = 0;
  public static final int RIGHT = 1;
  public static final int MIDDLE = 2;

  private final State state;
  private final int value;

  /**
   * Constructs a new {@link MouseButtonEvent} with the given state and button number
   *
   * @param state The new state the button is in
   * @param value The numerical id of the mouse button
   */
  public MouseButtonEvent(State state, int value) {
    this.state = state;
    this.value = value;
  }

  /**
   * Retrieves the new state of the mouse button
   *
   * @return The new state of the button with the id {@link #getValue()}
   */
  public State getState() {
    return state;
  }

  /**
   * Retrieves the numerical id of the mouse button which has changed the state
   *
   * @return The numerical id of the button which has changed
   */
  public int getValue() {
    return value;
  }

  /**
   * Simple representation of possible mouse button states
   */
  public enum State {
    /**
     * The button has been pressed
     */
    PRESS,

    /**
     * The button has been released
     */
    RELEASE,

    /**
     * The button is being hold for a long duration
     */
    REPEAT
  }
}
