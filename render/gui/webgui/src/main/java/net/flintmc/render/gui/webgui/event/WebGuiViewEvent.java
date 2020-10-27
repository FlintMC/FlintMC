package net.flintmc.render.gui.webgui.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.render.gui.webgui.WebGuiView;

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
