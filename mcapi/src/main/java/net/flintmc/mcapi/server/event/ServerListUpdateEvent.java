package net.flintmc.mcapi.server.event;

import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.server.ServerData;
import net.flintmc.mcapi.server.ServerList;

/**
 * This event will be fired when a server in the ServerList is added, removed or updated. It will
 * not be fired, when a server is updated in the vanilla server Gui, only when it is updated via
 * {@link ServerList#updateServerData(int, ServerData)}. It will be fired in both the {@link
 * Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phases, but cancellation will only have an
 * effect in the PRE phase.
 *
 * @see Subscribe
 */
public interface ServerListUpdateEvent extends Event, Cancellable {

  /**
   * Retrieves the index in the server list, 0 being the highest.
   *
   * @return The index which is always >= 0
   */
  int getIndex();

  /**
   * Retrieves the server data that has been added, updated or removed.
   *
   * @return The non-null server data of this event
   */
  ServerData getServerData();

  /**
   * Retrieves the type why this event has been fired.
   *
   * @return The non-null type of this event
   */
  Type getType();

  /**
   * All types why a {@link ServerListUpdateEvent} could be fired.
   */
  enum Type {
    /**
     * Fired when an entry is added to the {@link ServerList}.
     */
    ADD,
    /**
     * Fired when an entry is updated in the {@link ServerList}, e.g. when two entries are swapped,
     * two updates will be fired for each of them.
     */
    UPDATE,
    /**
     * Fired when an entry is removed from the {@link ServerList}.
     */
    REMOVE
  }

  /**
   * Factory for the {@link ServerListUpdateEvent}.
   */
  @AssistedFactory(ServerListUpdateEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerListUpdateEvent}.
     *
     * @param index      The index of the entry that has been updated, always >= 0
     * @param serverData The non-null data that has been updated
     * @param type       The non-null type of the new event
     * @return The new non-null {@link ServerListUpdateEvent}
     */
    ServerListUpdateEvent create(
        @Assisted int index, @Assisted ServerData serverData, @Assisted Type type);
  }
}
