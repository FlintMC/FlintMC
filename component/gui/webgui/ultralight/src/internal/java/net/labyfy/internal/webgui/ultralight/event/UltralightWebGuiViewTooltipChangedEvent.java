package net.labyfy.internal.webgui.ultralight.event;

import net.labyfy.webgui.WebGuiView;
import net.labyfy.webgui.event.WebGuiViewTooltipChangedEvent;

/**
 * Event fired when a {@link net.labyfy.internal.webgui.ultralight.view.UltralightWebGuiView} changes its tooltip.
 */
public class UltralightWebGuiViewTooltipChangedEvent extends UltralightWebGuiViewEvent
    implements WebGuiViewTooltipChangedEvent {
  private final String newTooltip;

  /**
   * Constructs a new {@link UltralightWebGuiViewTitleChangedEvent}.
   *
   * @param view       The view that sent the event
   * @param newTooltip The new tooltip of the view
   */
  public UltralightWebGuiViewTooltipChangedEvent(WebGuiView view, String newTooltip) {
    super(view);
    this.newTooltip = newTooltip;
  }

  @Override
  public String newTooltip() {
    return newTooltip;
  }
}
