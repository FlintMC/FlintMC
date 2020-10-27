package net.flintmc.mcapi.v1_15_2.player.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.type.EntityTypeRegister;
import net.flintmc.mcapi.player.RemoteClientPlayer;

/** 1.15.2 implementation of the {@link RemoteClientPlayer.Provider}. */
@Singleton
@Implement(value = RemoteClientPlayer.Provider.class, version = "1.15.2")
public class VersionedRemoteClientPlayerEntityProvider implements RemoteClientPlayer.Provider {

  private final RemoteClientPlayer.Factory remoteClientPlayerEntity;
  private final EntityTypeRegister entityTypeRegister;

  @Inject
  private VersionedRemoteClientPlayerEntityProvider(
      RemoteClientPlayer.Factory remoteClientPlayerEntity, EntityTypeRegister entityTypeRegister) {
    this.remoteClientPlayerEntity = remoteClientPlayerEntity;
    this.entityTypeRegister = entityTypeRegister;
  }

  /** {@inheritDoc} */
  @Override
  public RemoteClientPlayer get(Object entity) {
    return this.remoteClientPlayerEntity.create(
        entity, this.entityTypeRegister.getEntityType("player"));
  }
}
