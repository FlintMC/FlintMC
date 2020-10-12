package net.labyfy.internal.webgui.ultralight.event;

import net.labyfy.webgui.WebGuiView;
import net.labyfy.webgui.event.WebGuiViewCloseEvent;

public class UltralightWebGuiViewCloseEvent extends UltralightWebGuiViewEvent implements WebGuiViewCloseEvent {
  /**
   * Constructs a new {@link UltralightWebGuiViewCloseEvent} with the given view as the source.
   *
   * @param view The view that sent the event
   */
  public UltralightWebGuiViewCloseEvent(WebGuiView view) {
    super(view);
  }
}
