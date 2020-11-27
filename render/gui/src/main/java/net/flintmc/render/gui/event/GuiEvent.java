package net.flintmc.render.gui.event;

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.render.gui.windowing.Window;

/**
 * Marker interface for all input events. These events are all fired in the {@link
 * Subscribe.Phase#PRE} phase.
 *
 * @see Subscribe
 */
public interface GuiEvent extends Event, Cancellable {

  /**
   * Retrieves the window in which this event has happened.
   *
   * @return The non-null event where this event has happened
   */
  Window getWindow();
}
