package net.labyfy.internal.component.player.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.entity.type.EntityTypeRegister;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.PlayerEntity;

@Singleton
@Implement(value = PlayerEntity.Provider.class, version = "1.15.2")
public class VersionedPlayerEntityProvider implements PlayerEntity.Provider {

  private final PlayerEntity.Factory playerEntityFactory;
  private final EntityTypeRegister entityTypeRegister;

  @Inject
  public VersionedPlayerEntityProvider(
          PlayerEntity.Factory playerEntityFactory,
          EntityTypeRegister entityTypeRegister
  ) {
    this.playerEntityFactory = playerEntityFactory;
    this.entityTypeRegister = entityTypeRegister;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PlayerEntity get(Object entity) {
    return this.playerEntityFactory.create(
            entity,
            this.entityTypeRegister.getEntityType("player")
    );
  }
}
