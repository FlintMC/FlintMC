package net.flintmc.render.gui.event;

import net.flintmc.render.gui.windowing.Window;

/** Base class for {@link GuiEvent}s which means cancellable events with a window. */
public class DefaultGuiEvent implements GuiEvent {

  private final Window window;
  private boolean cancelled;

  /**
   * Constructs a new {@link DefaultGuiEvent} for the given window.
   *
   * @param window The non-null window where this event has happened
   */
  protected DefaultGuiEvent(Window window) {
    this.window = window;
  }

  @Override
  public Window getWindow() {
    return this.window;
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
