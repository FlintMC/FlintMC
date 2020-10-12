package net.labyfy.internal.webgui.ultralight.event;

import net.labyfy.webgui.WebGuiView;
import net.labyfy.webgui.event.WebGuiViewWindowObjectReadyEvent;

/**
 * Event fired when a {@link net.labyfy.internal.webgui.ultralight.view.UltralightWebGuiView} window object is ready.
 */
public class UltralightWebGuiViewWindowObjectReadyEvent extends UltralightWebGuiViewStateChangeEvent
  implements WebGuiViewWindowObjectReadyEvent {
  /**
   * Constructs a new {@link UltralightWebGuiViewWindowObjectReadyEvent}.
   *
   * @param view        The view that sent the event
   * @param frameId     The ID of the frame that sent the event
   * @param url         The URL the event originated from
   * @param isMainFrame Whether the event has been sent by a main frame
   */
  public UltralightWebGuiViewWindowObjectReadyEvent(WebGuiView view, long frameId, String url, boolean isMainFrame) {
    super(view, frameId, url, isMainFrame);
  }
}
