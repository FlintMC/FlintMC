package net.labyfy.internal.webgui.ultralight.event;

import net.labyfy.webgui.WebGuiView;
import net.labyfy.webgui.event.WebGuiViewURLChangedEvent;

/**
 * Event fired when a {@link net.labyfy.internal.webgui.ultralight.view.UltralightWebGuiView} changes its url.
 */
public class UltralightWebGuiViewURLChangedEvent extends UltralightWebGuiViewEvent implements WebGuiViewURLChangedEvent {
  private final String newURL;

  /**
   * Constructs a new {@link UltralightWebGuiViewURLChangedEvent}.
   *
   * @param view   The view that sent the event
   * @param newURL The new URL of the view
   */
  public UltralightWebGuiViewURLChangedEvent(WebGuiView view, String newURL) {
    super(view);
    this.newURL = newURL;
  }

  @Override
  public String newURL() {
    return newURL;
  }
}
