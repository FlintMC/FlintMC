package net.flintmc.mcapi.settings.game.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Fired when Minecraft loads or saves the its game settings. It will be fired on both the {@link
 * Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phases.
 *
 * @see Subscribe
 */
public interface GameSettingsStorageEvent extends Event {

  /**
   * Retrieves the state of this event.
   *
   * @return The current state.
   */
  State getState();

  /** An enumeration that representing all states for the configuration. */
  enum State {

    /** When the configuration is loaded. */
    LOAD,
    /** When the configuration is saved. */
    SAVE
  }

  /** A factory class for the {@link GameSettingsStorageEvent}. */
  @AssistedFactory(GameSettingsStorageEvent.class)
  interface Factory {

    /**
     * Creates a new {@link GameSettingsStorageEvent} with the given parameters.
     *
     * @param state The state when the event is fired.
     * @return A created configuration event.
     */
    GameSettingsStorageEvent create(@Assisted("state") State state);
  }
}
