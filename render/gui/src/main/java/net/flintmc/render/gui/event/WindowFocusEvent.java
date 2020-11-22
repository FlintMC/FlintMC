package net.flintmc.render.gui.event;

import net.flintmc.render.gui.windowing.Window;

/** Event indicating that a window got or lost focus. */
public class WindowFocusEvent extends DefaultGuiEvent implements GuiEvent {
  private final boolean isFocused;

  /**
   * Constructs a new {@link WindowFocusEvent} with the specified focus state.
   *
   * @param window The non-null window where this event has happened
   * @param isFocused The new focus state of the window
   */
  public WindowFocusEvent(Window window, boolean isFocused) {
    super(window);
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
