package net.flintmc.mcapi.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * This event will be fired when the player closes the ingame menu when being either in Singleplayer
 * or Multiplayer. This is is done with the ESCAPE key or the "Back to Game" button. This event will
 * only be fired in the {@link Subscribe.Phase#PRE} phase.
 *
 * @see Subscribe
 */
public interface IngameMenuCloseEvent extends Event {

  /** Factory for the {@link IngameMenuCloseEvent}. */
  @AssistedFactory(IngameMenuCloseEvent.class)
  interface Factory {

    /**
     * Creates a new {@link IngameMenuCloseEvent}.
     *
     * @return The new non-null {@link IngameMenuCloseEvent}
     */
    IngameMenuCloseEvent create();
  }
}
