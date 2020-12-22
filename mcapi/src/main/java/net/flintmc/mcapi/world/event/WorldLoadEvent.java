package net.flintmc.mcapi.world.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * This event will be fired when a world is being loaded (e.g. when clicking on "Play selected
 * world" in the Singleplayer screen). It will only be fired in the {@link Phase#POST} phase.
 *
 * @see Subscribe
 */
public interface WorldLoadEvent extends Event {

  /**
   * Retrieves the name of the world that is being loaded.
   *
   * @return The non-null name of the world
   */
  String getWorldName();

  /**
   * Retrieves the current state in the loading phase.
   *
   * @return The non-null state in this event
   */
  State getState();

  /**
   * The percentage of the loading phase.
   *
   * <p>If the {@link #getState() state} is {@link State#START}, this will be 0.
   *
   * <p>If the {@link #getState() state} is {@link State#END}, this will be 100.
   *
   * @return The percentage from 0 to 100
   */
  float getProcessPercentage();

  /**
   * States in the loading phase in a {@link WorldLoadEvent}.
   */
  enum State {
    /**
     * The loading of the world has just started.
     */
    START,
    /**
     * Another chunk has been loaded and the percentage has been updated.
     */
    UPDATE,
    /**
     * All required chunks have been loaded the loading is done.
     */
    END
  }

  /**
   * Factory for the {@link WorldLoadEvent}.
   */
  @AssistedFactory(WorldLoadEvent.class)
  interface Factory {

    /**
     * Creates a new {@link WorldLoadEvent}.
     *
     * @param worldName         The non-null name of the world that is being loaded
     * @param state             The non-null state in loading the world
     * @param processPercentage The percentage of the completion in loading the world from 0 to 100
     * @return The new non-null {@link WorldLoadEvent}
     */
    WorldLoadEvent create(
        @Assisted String worldName, @Assisted State state, @Assisted float processPercentage);
  }
}
