package net.labyfy.webgui;

/**
 * Represents the {@link WebGuiView} inside the main minecraft window.
 */
public interface MainWebGuiView extends WebGuiView {
  /**
   * Enables or disables focus for this view. This is different from the window focus, as that both the window focus
   * and this need to be true in order for the view to gain focus.
   *
   * @param allow Whether focusing the view is allowed
   */
  void setAllowFocus(boolean allow);
}
