package net.flintmc.mcapi.potion.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.potion.effect.StatusEffect;
import net.flintmc.mcapi.potion.effect.StatusEffectInstance;

/** Base event for the {@link PotionAddEvent} and {@link PotionRemoveEvent}. */
public interface PotionEvent extends Event {

  /**
   * Retrieves the living entity where the status effect is added or removed.
   *
   * @return The living entity where the status effect is added or removed.
   */
  LivingEntity getLivingEntity();

  /**
   * Retrieves the type when the event is fired.
   *
   * @return The type when the event is fired.
   */
  Type getType();

  enum Type {
    ADD,
    REMOVE
  }
}
