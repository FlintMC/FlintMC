package net.labyfy.internal.webgui.ultralight.event;

import net.labyfy.webgui.WebGuiView;
import net.labyfy.webgui.event.WebGuiViewDOMReadyEvent;

/**
 * Event fired when a {@link net.labyfy.internal.webgui.ultralight.view.UltralightWebGuiView} DOM is ready.
 */
public class UltralightWebGuiViewDOMReadyEvent extends UltralightWebGuiViewStateChangeEvent
    implements WebGuiViewDOMReadyEvent {
  /**
   * Constructs a new {@link UltralightWebGuiViewDOMReadyEvent}.
   *
   * @param view        The view that sent the event
   * @param frameId     The ID of the frame that sent the event
   * @param url         The URL the event originated from
   * @param isMainFrame Whether the event has been sent by a main frame
   */
  public UltralightWebGuiViewDOMReadyEvent(WebGuiView view, long frameId, String url, boolean isMainFrame) {
    super(view, frameId, url, isMainFrame);
  }
}
