package net.flintmc.render.gui.webgui.event;

import net.flintmc.render.gui.webgui.WebGuiView;

/** Event indicating a title of a {@link WebGuiView} changed. */
public interface WebGuiViewTitleChangedEvent extends WebGuiViewEvent {
  /**
   * Retrieves the new title of the view.
   *
   * @return The new title
   */
  String newTitle();
}
