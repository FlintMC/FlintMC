package net.flintmc.mcapi.potion.event;

import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.potion.effect.StatusEffect;

/**
 * Fired when a status effect is removed from a {@link LivingEntity}.
 *
 * <p>The event is fired in the {@link Subscribe.Phase#PRE} phase.
 *
 * @see Subscribe.Phase#PRE
 */
public interface PotionRemoveEvent extends PotionEvent {

  /**
   * Retrieves the status effect to be removed.
   *
   * @return The status effect to be removed.
   */
  StatusEffect getStatusEffect();

  /**
   * Factory for the {@link PotionRemoveEvent}.
   */
  @AssistedFactory(PotionRemoveEvent.class)
  interface Factory {

    /**
     * Creates a new {@link PotionRemoveEvent} with the given {@code livingEntity} and the {@code
     * statusEffect}.
     *
     * @param livingEntity The living entity for which the status effect is to be removed.
     * @param statusEffect The status effect to be removed.
     * @return A created potion remove event.
     */
    PotionRemoveEvent create(
        @Assisted("livingEntity") LivingEntity livingEntity,
        @Assisted("statusEffect") StatusEffect statusEffect);
  }
}
