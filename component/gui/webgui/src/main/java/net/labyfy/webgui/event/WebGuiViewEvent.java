package net.labyfy.webgui.event;

import net.labyfy.component.eventbus.event.Event;
import net.labyfy.webgui.WebGuiView;

/**
 * Base interface for all events sent from {@link WebGuiView}s.
 */
public interface WebGuiViewEvent extends Event {
  /**
   * Retrieves the {@link WebGuiView} that sent the event.
   *
   * @return The {@link WebGuiView} that sent the event
   */
  WebGuiView view();
}
