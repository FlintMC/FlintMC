package net.labyfy.internal.webgui.ultralight.event;

import net.labyfy.webgui.WebGuiView;
import net.labyfy.webgui.event.WebGuiViewNavigateEvent;

public class UltralightWebGuiViewNavigateEvent extends UltralightWebGuiViewEvent implements WebGuiViewNavigateEvent {
  /**
   * Constructs a new {@link UltralightWebGuiViewNavigateEvent} with the given view as the source.
   *
   * @param view The view that sent the event
   */
  public UltralightWebGuiViewNavigateEvent(WebGuiView view) {
    super(view);
  }
}
