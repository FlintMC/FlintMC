package net.flintmc.mcapi.potion.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.mcapi.entity.LivingEntity;

/**
 * Base event for the {@link PotionAddEvent} and {@link PotionRemoveEvent}.
 */
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
