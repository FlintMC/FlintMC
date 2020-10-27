package net.flintmc.render.gui.event;

/**
 * Listener interface for {@link GuiEvent}s.
 */
public interface GuiEventListener {
  /**
   * Called when an event is fired.
   *
   * @param event The event that has been fired
   * @return {@code true} to consume the event and prevent further handling, {@code false} to pass the event on
   */
  boolean handle(GuiEvent event);
}
