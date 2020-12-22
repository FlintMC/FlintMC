package net.flintmc.mcapi.internal.entity.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.event.EntitySpawnEvent;

/** {@inheritDoc} */
@Implement(EntitySpawnEvent.class)
public class DefaultEntitySpawnEvent implements EntitySpawnEvent {

  private final int identifier;
  private final Entity entity;

  @AssistedInject
  public DefaultEntitySpawnEvent(@Assisted int identifier, @Assisted Entity entity) {
    this.identifier = identifier;
    this.entity = entity;
  }

  /** {@inheritDoc} */
  @Override
  public int getIdentifier() {
    return this.identifier;
  }

  /** {@inheritDoc} */
  @Override
  public Entity getEntity() {
    return this.entity;
  }
}
