package net.flintmc.mcapi.potion.event;

import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.potion.effect.StatusEffectInstance;

/**
 * Fired when a status effect is added to a {@link LivingEntity}.
 *
 * <p>The event is fired in the {@link Subscribe.Phase#PRE} phase.
 *
 * @see Subscribe.Phase#PRE
 */
public interface PotionAddEvent extends PotionEvent {

  /**
   * Retrieves the status effect instance to be added.
   *
   * @return The status effect instance to be added.
   */
  StatusEffectInstance getStatusEffectInstance();

  /**
   * Factory for {@link PotionAddEvent}
   */
  @AssistedFactory(PotionAddEvent.class)
  interface Factory {

    /**
     * Creates a new {@link PotionAddEvent} with the given {@code livingEntity} and the {@code
     * statusEffectInstance}.
     *
     * @param livingEntity         The living entity for which the status effect is to be added.
     * @param statusEffectInstance The status effect instance to be added.
     * @return A created potion add event.
     */
    PotionAddEvent create(
        @Assisted("livingEntity") LivingEntity livingEntity,
        @Assisted("statusEffectInstance") StatusEffectInstance statusEffectInstance);
  }
}
