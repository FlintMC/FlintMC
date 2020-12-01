package net.flintmc.mcapi.internal.entity.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.event.EntityAttackEvent;

/** {@inheritDoc} */
@Implement(EntityAttackEvent.class)
public class DefaultEntityAttackEvent implements EntityAttackEvent {

  private final Entity attacked;

  @AssistedInject
  public DefaultEntityAttackEvent(@Assisted Entity attacked) {
    this.attacked = attacked;
  }

  /** {@inheritDoc} */
  @Override
  public Entity getAttacked() {
    return this.attacked;
  }
}
