package net.labyfy.internal.webgui.ultralight.event;

import net.labyfy.webgui.WebGuiView;
import net.labyfy.webgui.event.WebGuiViewEvent;

/** Base class for all ultralight {@link WebGuiViewEvent}s. */
public abstract class UltralightWebGuiViewEvent implements WebGuiViewEvent {
  private final WebGuiView view;

  /**
   * Constructs a new {@link UltralightWebGuiViewEvent} with the given view as the source.
   *
   * @param view The view that sent the event
   */
  protected UltralightWebGuiViewEvent(WebGuiView view) {
    this.view = view;
  }

  /** {@inheritDoc} */
  @Override
  public WebGuiView view() {
    return view;
  }
}
