package net.flintmc.mcapi.event;

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * This event will be fired when the player opens the ingame menu when being either in Singleplayer
 * or Multiplayer. This is is done with the ESCAPE key. This event will only be fired in the {@link
 * Subscribe.Phase#PRE} phase.
 *
 * @see Subscribe
 */
public interface IngameMenuOpenEvent extends Event, Cancellable {

  /** Factory for the {@link IngameMenuOpenEvent}. */
  @AssistedFactory(IngameMenuOpenEvent.class)
  interface Factory {

    /**
     * Creates a new {@link IngameMenuOpenEvent}.
     *
     * @return The new non-null {@link IngameMenuOpenEvent}
     */
    IngameMenuOpenEvent create();
  }
}
