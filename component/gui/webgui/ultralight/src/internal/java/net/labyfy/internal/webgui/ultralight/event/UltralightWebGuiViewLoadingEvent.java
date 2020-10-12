package net.labyfy.internal.webgui.ultralight.event;

import net.labyfy.webgui.WebGuiView;
import net.labyfy.webgui.event.WebGuiViewLoadingEvent;

/**
 * Event fired when a {@link net.labyfy.internal.webgui.ultralight.view.UltralightWebGuiView} changes its loading
 * status.
 */
public class UltralightWebGuiViewLoadingEvent extends UltralightWebGuiViewStateChangeEvent
    implements WebGuiViewLoadingEvent {
  private final ErrorInfo errorInfo;

  /**
   * Constructs a new {@link UltralightWebGuiViewStateChangeEvent}.
   *
   * @param view      The view that sent the event
   * @param frameId   The ID of the frame that sent the event
   * @param url       The URL the event originated from
   * @param mainFrame Whether the event has been sent by a main frame
   * @param errorInfo Information about an error that occurred, or {@code null}, if none
   */
  public UltralightWebGuiViewLoadingEvent(
      WebGuiView view, long frameId, String url, boolean mainFrame, ErrorInfo errorInfo) {
    super(view, frameId, url, mainFrame);
    this.errorInfo = errorInfo;
  }

  @Override
  public ErrorInfo errorInfo() {
    return errorInfo;
  }
}
