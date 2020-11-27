package net.flintmc.mcapi.player.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/** Fired when the player's field of view is rendered. */
public interface FieldOfViewEvent extends Event {

  /**
   * Retrieves the field of view of the player entity.
   *
   * @return The player's field of view.
   */
  float getFov();

  /**
   * Changes the field of view of the player entity.
   *
   * @param fov The new field of view.
   */
  void setFov(float fov);

  /** A factory class for the {@link FieldOfViewEvent}. */
  @AssistedFactory(FieldOfViewEvent.class)
  interface Factory {

    /**
     * Creates a new field of view event.
     *
     * @param fov The field of view.
     * @return The created field of view event.
     */
    FieldOfViewEvent create(@Assisted("fov") float fov);
  }
}
