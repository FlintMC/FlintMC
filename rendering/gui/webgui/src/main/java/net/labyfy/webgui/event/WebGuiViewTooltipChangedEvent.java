package net.labyfy.webgui.event;

/**
 * Event indicating the tooltip of a {@link net.labyfy.webgui.WebGuiView} changed.
 */
public interface WebGuiViewTooltipChangedEvent extends WebGuiViewEvent {
  /**
   * Retrieves the new tooltip of the view.
   *
   * @return The new tooltip
   */
  String newTooltip();
}
