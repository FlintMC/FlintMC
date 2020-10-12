package net.labyfy.webgui.event;

/**
 * Event indicating that a {@link net.labyfy.webgui.WebGuiView} state has changed.
 */
public interface WebGuiViewStateChangeEvent extends WebGuiViewEvent {
  /**
   * Retrieves the ID of the frame that sent the event.
   *
   * @return The ID of the source frame
   */
  long frameId();

  /**
   * Retrieves the URL of the frame that sent the event.
   *
   * @return The url of the frame
   */
  String url();

  /**
   * Determines whether the event comes from the main frame or some sub frame that sent the event.
   *
   * @return {@code true} if the event has been fired by the main frame, {@code false} otherwise
   */
  boolean isMainFrame();
}
