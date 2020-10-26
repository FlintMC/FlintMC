package net.labyfy.webgui.event;

/**
 * Event indicating that a {@link net.labyfy.webgui.WebGuiView}s URL has changed.
 */
public interface WebGuiViewURLChangedEvent extends WebGuiViewEvent {
  /**
   * Retrieves the new URL of the {@link net.labyfy.webgui.WebGuiView}.
   *
   * @return The new url
   */
  String newURL();
}
