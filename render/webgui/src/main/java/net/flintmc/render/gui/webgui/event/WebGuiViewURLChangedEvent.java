package net.flintmc.render.gui.webgui.event;

import net.flintmc.render.gui.webgui.WebGuiView;

/** Event indicating that a {@link WebGuiView}s URL has changed. */
public interface WebGuiViewURLChangedEvent extends WebGuiViewEvent {
  /**
   * Retrieves the new URL of the {@link WebGuiView}.
   *
   * @return The new url
   */
  String newURL();
}
