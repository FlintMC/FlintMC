package net.labyfy.component.gui.event;

/**
 * Represents a processor of input events.
 */
public interface GuiInputEventProcessor {
  /**
   * Called by the {@link net.labyfy.component.gui.GuiController} to signal an event.
   *
   * @param event The event that occurred
   * @return {@code true} to consume the event and prevent Minecraft from receiving it, {@code false} to pass it on
   */
  boolean process(GuiInputEvent event);
}
