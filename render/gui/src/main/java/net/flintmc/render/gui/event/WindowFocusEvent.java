package net.flintmc.render.gui.event;

/** Event indicating that a window got or lost focus. */
public class WindowFocusEvent implements GuiEvent {
  private final boolean isFocused;

  /**
   * Constructs a new {@link WindowFocusEvent} with the specified focus state.
   *
   * @param isFocused The new focus state of the window
   */
  public WindowFocusEvent(boolean isFocused) {
    this.isFocused = isFocused;
  }

  /**
   * Determines whether the window is focused or unfocused now.
   *
   * @return {@code true} if the window is focused now, {@code false} otherwise
   */
  public boolean isFocused() {
    return isFocused;
  }
}
