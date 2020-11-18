package net.flintmc.mcapi.v1_15_2.entity.type;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.EntitySize;
import net.flintmc.mcapi.entity.type.EntityType;

/** 1.15.2 implementation of the {@link EntityType}. */
@Implement(value = EntityType.class, version = "1.15.2")
public class VersionedEntityType implements EntityType {

  private final Entity.Classification classification;
  private final boolean serializable;
  private final boolean summonable;
  private final boolean immuneToFire;
  private final boolean canSpawnFarFromPlayer;
  private final EntitySize entitySize;

  @AssistedInject
  private VersionedEntityType(
      @Assisted("classification") Entity.Classification classification,
      @Assisted("serializable") boolean serializable,
      @Assisted("summonable") boolean summonable,
      @Assisted("immuneToFire") boolean immuneToFire,
      @Assisted("canSpawnFarFromPlayer") boolean canSpawnFarFromPlayer,
      @Assisted("entitySize") EntitySize entitySize) {
    this.classification = classification;
    this.serializable = serializable;
    this.summonable = summonable;
    this.immuneToFire = immuneToFire;
    this.canSpawnFarFromPlayer = canSpawnFarFromPlayer;
    this.entitySize = entitySize;
  }

  /** {@inheritDoc} */
  @Override
  public Entity.Classification getClassification() {
    return this.classification;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSerializable() {
    return this.serializable;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSummonable() {
    return this.summonable;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isImmuneToFire() {
    return this.immuneToFire;
  }

  /** {@inheritDoc} */
  @Override
  public boolean canSpawnFarFromPlayer() {
    return this.canSpawnFarFromPlayer;
  }

  /** {@inheritDoc} */
  @Override
  public EntitySize getSize() {
    return this.entitySize;
  }
}