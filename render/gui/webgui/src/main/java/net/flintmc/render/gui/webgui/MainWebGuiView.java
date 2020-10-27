package net.flintmc.render.gui.webgui;

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

  /**
   * Sets the visibility of main view. It will not receive input events when it is invisible, regardless of what
   * the focus allowance directs.
   *
   * @param visible If {@code true}, the view will be shown, if {@code false}, it will be hidden
   */
  void setVisible(boolean visible);
}
