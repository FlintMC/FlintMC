package net.flintmc.mcapi.world.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * This event will be fried when a world is being unloaded. It will only be fried in the {@link
 * Subscribe.Phase#POST} phase.
 *
 * @see Subscribe
 */
public interface WorldUnloadEvent extends Event {

  /**
   * A factory for {@link WorldUnloadEvent}.
   */
  @AssistedFactory(WorldUnloadEvent.class)
  interface Factory {

    /**
     * Creates a new {@link WorldUnloadEvent}.
     *
     * @return A created world unload event.
     */
    WorldUnloadEvent create();

  }

}
