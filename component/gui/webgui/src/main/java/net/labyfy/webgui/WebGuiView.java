package net.labyfy.webgui;

/**
 * Represents a single view (imagine like a browser tab) which can display content.
 */
public interface WebGuiView {
  /**
   * Closes the view and disposes its native resources. Note that closing the view can have further impact such
   * as closing the window if the view has wrapped a window.
   */
  void close();

  /**
   * Navigates the view to the given URL.
   *
   * @param url The url to navigate to
   */
  void setURL(String url);
}
