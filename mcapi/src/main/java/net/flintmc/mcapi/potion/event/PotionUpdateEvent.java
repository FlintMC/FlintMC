package net.flintmc.mcapi.potion.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.LivingEntity;

/**
 * Fired when the potions are to be updated.
 *
 * <p>The event is fired in the {@link Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phase.
 */
public interface PotionUpdateEvent extends Event {

  /**
   * Retrieves the living entity where the potion are updated.
   *
   * @return The living entity where the potion are updated.
   */
  LivingEntity getLivingEntity();

  /**
   * Factory for {@link PotionUpdateEvent}.
   */
  @AssistedFactory(PotionUpdateEvent.class)
  interface Factory {

    /**
     * Creates a new {@link PotionUpdateEvent} with the given {@code livingEntity}.
     *
     * @param livingEntity The living entity where the potions are updated.
     * @return A created potion update event.
     */
    PotionUpdateEvent create(@Assisted("livingEntity") LivingEntity livingEntity);
  }
}
