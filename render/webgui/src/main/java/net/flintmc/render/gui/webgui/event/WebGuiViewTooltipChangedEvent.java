package net.flintmc.render.gui.webgui.event;

import net.flintmc.render.gui.webgui.WebGuiView;

/** Event indicating the tooltip of a {@link WebGuiView} changed. */
public interface WebGuiViewTooltipChangedEvent extends WebGuiViewEvent {
  /**
   * Retrieves the new tooltip of the view.
   *
   * @return The new tooltip
   */
  String newTooltip();
}
