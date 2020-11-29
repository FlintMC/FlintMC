package net.flintmc.mcapi.internal.entity.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.event.EntityDespawnEvent;

@Implement(EntityDespawnEvent.class)
public class DefaultEntityDespawnEvent implements EntityDespawnEvent {

  private final Entity entity;

  @AssistedInject
  public DefaultEntityDespawnEvent(@Assisted Entity entity) {
    this.entity = entity;
  }

  @Override
  public Entity getEntity() {
    return this.entity;
  }
}
