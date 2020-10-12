package net.labyfy.internal.webgui.ultralight.event;

import net.labyfy.webgui.WebGuiView;
import net.labyfy.webgui.event.WebGuiViewStateChangeEvent;

/**
 * Event fired when a {@link net.labyfy.internal.webgui.ultralight.view.UltralightWebGuiView} changes its state.
 */
public class UltralightWebGuiViewStateChangeEvent extends UltralightWebGuiViewEvent
    implements WebGuiViewStateChangeEvent {
  private final long frameId;
  private final String url;
  private final boolean isMainFrame;

  /**
   * Constructs a new {@link UltralightWebGuiViewStateChangeEvent}.
   *
   * @param view        The view that sent the event
   * @param frameId     The ID of the frame that sent the event
   * @param url         The URL the event originated from
   * @param isMainFrame Whether the event has been sent by a main frame
   */
  public UltralightWebGuiViewStateChangeEvent(
      WebGuiView view, long frameId, String url, boolean isMainFrame) {
    super(view);
    this.frameId = frameId;
    this.url = url;
    this.isMainFrame = isMainFrame;
  }

  @Override
  public long frameId() {
    return frameId;
  }

  @Override
  public String url() {
    return url;
  }

  @Override
  public boolean isMainFrame() {
    return isMainFrame;
  }
}
