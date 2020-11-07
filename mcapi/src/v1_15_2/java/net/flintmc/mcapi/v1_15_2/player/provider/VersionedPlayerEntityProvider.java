package net.flintmc.mcapi.v1_15_2.player.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.type.EntityTypeRegister;
import net.flintmc.mcapi.player.PlayerEntity;

/** 1.15.2 implementation of the {@link PlayerEntity.Provider}. */
@Singleton
@Implement(value = PlayerEntity.Provider.class, version = "1.15.2")
public class VersionedPlayerEntityProvider implements PlayerEntity.Provider {

  private final PlayerEntity.Factory playerEntityFactory;
  private final EntityTypeRegister entityTypeRegister;

  @Inject
  public VersionedPlayerEntityProvider(
      PlayerEntity.Factory playerEntityFactory, EntityTypeRegister entityTypeRegister) {
    this.playerEntityFactory = playerEntityFactory;
    this.entityTypeRegister = entityTypeRegister;
  }

  /** {@inheritDoc} */
  @Override
  public PlayerEntity get(Object entity) {
    return this.playerEntityFactory.create(entity, this.entityTypeRegister.getEntityType("player"));
  }
}
