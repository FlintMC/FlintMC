package net.labyfy.component.gui.event;

/**
 * Represents a processor of input events.
 */
public interface GuiInputEventProcessor {
  /**
   * Called by the {@link net.labyfy.component.gui.GuiController} to indicate that input is about to be processed.
   * May only be called when the input has not begun already.
   */
  void beginInput();

  /**
   * Called by the {@link net.labyfy.component.gui.GuiController} to signal an event. Must be called
   * between a pair of {@link #beginInput()} and {@link #endInput()} calls.
   *
   * @param event The event that occurred
   * @return Wether the event should be consumed and not passed on
   */
  boolean process(GuiInputEvent event);

  /**
   * Called by the {@link net.labyfy.component.gui.GuiController} to indicate that the input processing has ended.
   * May only be called when the has begun and not ended yet. Moreover, no more calls to {@link #process(GuiInputEvent)}
   * are allowed, unless {@link #beginInput()} is called again.
   */
  void endInput();
}
