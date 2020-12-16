package net.flintmc.mcapi.event;

import net.flintmc.framework.eventbus.event.subscribe.Subscribe;

/**
 * The base event for communication between two applications (e.g. a client and server) with a
 * specifiable direction (sending, receiving).
 *
 * @see Subscribe
 */
public interface DirectionalEvent {

  /**
   * Retrieves the direction in which this event has happened.
   *
   * @return The non-null direction of the action of this event
   */
  Direction getDirection();

  /**
   * An enumeration with all possible directions for the {@link DirectionalEvent}.
   */
  enum Direction {

    /**
     * The direction for receiving something from somewhere else (e.g. a server).
     */
    RECEIVE,
    /**
     * The direction for sending something to somewhere else (e.g. a server).
     */
    SEND
  }
}
