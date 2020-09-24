package net.labyfy.component.eventbus.event.entity;

public class EntitySpawnEvent extends EntityEvent {

  private final int entityId;

  public EntitySpawnEvent(int entityId, Object entity) {
    super(entity);
    this.entityId = entityId;
  }

  public int getEntityId() {
    return entityId;
  }
}
