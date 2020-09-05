package net.labyfy.webgui;

import java.util.Collection;

/**
 * General tooling around the web GUI components.
 */
public interface WebGuiController {
  /**
   * Retrieves a <b>read-only</b> collection of all currently active views.
   *
   * @return A collection of all active views
   */
  Collection<WebGuiView> getViews();

  /**
   * Retrieves the main view, also known as the view displayed in the main game window.
   *
   * @return The main view
   */
  WebGuiView getMainView();
}
