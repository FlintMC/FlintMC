package net.labyfy.webgui.event;

/**
 * Event indicating a title of a {@link net.labyfy.webgui.WebGuiView} changed.
 */
public interface WebGuiViewTitleChangedEvent extends WebGuiViewEvent {
  /**
   * Retrieves the new title of the view.
   *
   * @return The new title
   */
  String newTitle();
}
