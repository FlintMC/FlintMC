package net.flintmc.mcapi.potion.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.potion.effect.StatusEffectInstance;

/**
 * Fired when a potion is changed the state.
 *
 * <p>The event is fired in the {@link Subscribe.Phase#PRE}.
 *
 * @see Subscribe
 */
public interface PotionStateUpdateEvent extends Event {

  /**
   * Retrieves the living entity where the state of a potion was updated.
   *
   * @return The living entity where the state of a potion was updated.
   */
  LivingEntity getLivingEntity();

  /**
   * Retrieves the status effect instance which is updated.
   *
   * @return The status effect instance which is updated.
   */
  StatusEffectInstance getStatusEffectInstance();

  /**
   * Retrieves state of the update.
   *
   * @return The state of the update.
   */
  State getState();

  /**
   * An enumeration that representing all available states of the potion update event.
   */
  enum State {
    NEW,
    CHANGED,
    FINISHED
  }

  /**
   * Factory for {@link PotionStateUpdateEvent}.
   */
  @AssistedFactory(PotionStateUpdateEvent.class)
  interface Factory {

    /**
     * Creates a new {@link PotionStateUpdateEvent} with the given
     *
     * @param livingEntity The living entity for the potion state update event.
     * @param effect       The effect which is updated.
     * @param state        The state of the potion update event.
     * @return A created potion state update event.
     */
    PotionStateUpdateEvent create(
        @Assisted("livingEntity") LivingEntity livingEntity,
        @Assisted("effect") StatusEffectInstance effect,
        @Assisted("state") State state);
  }
}
