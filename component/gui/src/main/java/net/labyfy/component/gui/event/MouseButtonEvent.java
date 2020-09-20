package net.labyfy.component.gui.event;

import net.labyfy.component.gui.event.input.InputState;
import net.labyfy.component.gui.event.input.ModifierKey;
import net.labyfy.component.gui.event.input.MouseButton;

import java.util.Set;

/**
 * Event indicating that a mouse button state has changed or is still being hold.
 */
public class MouseButtonEvent extends EventWithModifierKeys {
  private final MouseButton button;
  private final InputState state;

  /**
   * Constructs a new {@link MouseButtonEvent} with the given state, button and modifier keys.
   *
   * @param state        The new state the button is in
   * @param button       The mouse button that has changed state
   * @param modifierKeys The modifier keys which were active while the event was fired
   */
  public MouseButtonEvent(MouseButton button, InputState state, Set<ModifierKey> modifierKeys) {
    super(modifierKeys);
    this.button = button;
    this.state = state;
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
}
