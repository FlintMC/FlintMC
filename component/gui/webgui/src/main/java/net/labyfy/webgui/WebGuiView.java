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
   * Enables or disables transparency for the view. The effects of this depend on the view and what kind of
   * object it is rendered to. This is mainly intended to boost performance of the main minecraft window, since
   * when the main view is not transparent, rendering the world can be skipped. Views other than the main view may
   * or may not respect it.
   *
   * @param transparent {@code true} if the view should become transparent, {@code false} to make the view opaque
   */
  void setTransparent(boolean transparent);

  /**
   * Navigates the view to the given URL.
   *
   * @param url The url to navigate to
   */
  void setURL(String url);

  /**
   * Retrieves the current URL of the view.
   *
   * @return The current view URL
   */
  String getURL();

  /**
   * Changes the scale of the view.
   *
   * @param scale The new scale
   */
  void setScale(float scale);
}
