package net.labyfy.internal.component.player.v1_15_2.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.entity.type.EntityTypeRegister;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.RemoteClientPlayer;

/**
 * 1.15.2 implementation of the {@link RemoteClientPlayer.Provider}.
 */
@Singleton
@Implement(value = RemoteClientPlayer.Provider.class, version = "1.15.2")
public class VersionedRemoteClientPlayerEntityProvider implements RemoteClientPlayer.Provider {

  private final RemoteClientPlayer.Factory remoteClientPlayerEntity;
  private final EntityTypeRegister entityTypeRegister;

  @Inject
  private VersionedRemoteClientPlayerEntityProvider(
          RemoteClientPlayer.Factory remoteClientPlayerEntity,
          EntityTypeRegister entityTypeRegister
  ) {
    this.remoteClientPlayerEntity = remoteClientPlayerEntity;
    this.entityTypeRegister = entityTypeRegister;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteClientPlayer get(Object entity) {
    return this.remoteClientPlayerEntity.create(
            entity,
            this.entityTypeRegister.getEntityType("player")
    );
  }
}
