package net.flintmc.render.gui.event;

import net.flintmc.render.gui.input.InputState;
import net.flintmc.render.gui.input.ModifierKey;
import net.flintmc.render.gui.input.MouseButton;
import net.flintmc.render.gui.windowing.Window;

import java.util.Set;

/** Event indicating that a mouse button state has changed or is still being hold. */
public class MouseButtonEvent extends EventWithModifierKeys {
  private final MouseButton button;
  private final InputState state;
  private final double x;
  private final double y;

  /**
   * Constructs a new {@link MouseButtonEvent} with the given state, button and modifier keys.
   *
   * @param window The non-null window where this event has happened
   * @param state The new state the button is in
   * @param button The mouse button that has changed state
   * @param x The x coordinate of the event
   * @param y The y coordinate of the event
   * @param modifierKeys The modifier keys which were active while the event was fired
   */
  public MouseButtonEvent(
          Window window, MouseButton button, InputState state, double x, double y, Set<ModifierKey> modifierKeys) {
    super(window, modifierKeys);
    this.button = button;
    this.state = state;
    this.x = x;
    this.y = y;
  }

  /**
   * Retrieves the new state of the mouse button
   *
   * @return The new state of the button
   */
  public InputState getState() {
    return state;
  }

  /**
   * Retrieves the mouse button that has changed state.
   *
   * @return The button that has changed state
   */
  public MouseButton getButton() {
    return button;
  }

  /**
   * Retrieves the x coordinate of the event.
   *
   * @return The x coordinate
   */
  public double getX() {
    return x;
  }

  /**
   * Retrieves the y coordinate of the event.
   *
   * @return The y coordinate
   */
  public double getY() {
    return y;
  }
}
