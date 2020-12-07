package net.flintmc.mcapi.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * This event will be fired every tick. It will be fired in both PRE and POST phases.
 *
 * @see Subscribe
 */
public interface TickEvent extends Event {

  /**
   * Retrieves the type of the tick in this event.
   *
   * @return The non-null type
   */
  Type getType();

  /** An enumeration of the states in the tick. */
  enum Type {

    /** This type will be fired at the beginning and the ending of one full tick. */
    GENERAL,
    /**
     * This type will be fired at the beginning and the ending of one game render tick, which means
     * that it will only be fired when the user is in game and doesn't have paused the game.
     */
    GAME_RENDER,
    /**
     * This type will be fired at the beginning and the ending of one world render tick (directly
     * after the GAME_RENDER tick), which means that it will only be fired when the user is in game
     * and doesn't have paused the game.
     */
    WORLD_RENDER
  }

  /** Factory for the {@link TickEvent}. */
  @AssistedFactory(TickEvent.class)
  interface Factory {

    /**
     * Creates a new {@link TickEvent} with the given type.
     *
     * @param type The non-null type of the new event
     * @return The new non-nul event
     */
    TickEvent create(@Assisted("type") Type type);
  }
}
