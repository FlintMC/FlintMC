package net.labyfy.internal.webgui.ultralight.event;

import net.labyfy.webgui.WebGuiView;
import net.labyfy.webgui.event.WebGuiViewTitleChangedEvent;

/**
 * Event fired when a {@link net.labyfy.internal.webgui.ultralight.view.UltralightWebGuiView} changes its title.
 */
public class UltralightWebGuiViewTitleChangedEvent extends UltralightWebGuiViewEvent
    implements WebGuiViewTitleChangedEvent {
  private final String newTitle;

  /**
   * Constructs a new {@link UltralightWebGuiViewTitleChangedEvent}.
   *
   * @param view     The view that sent the event
   * @param newTitle The new title of the view
   */
  public UltralightWebGuiViewTitleChangedEvent(WebGuiView view, String newTitle) {
    super(view);
    this.newTitle = newTitle;
  }

  @Override
  public String newTitle() {
    return newTitle;
  }
}
