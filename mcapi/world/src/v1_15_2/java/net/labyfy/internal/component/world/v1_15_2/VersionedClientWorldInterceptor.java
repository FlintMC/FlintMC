package net.labyfy.internal.component.world.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.mapper.EntityMapper;
import net.labyfy.component.entity.passive.PassiveEntityMapper;
import net.labyfy.component.player.ClientPlayer;
import net.labyfy.component.player.RemoteClientPlayer;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.component.world.ClientWorld;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.passive.PigEntity;

/**
 * 1.15.2 implementation of the client world interceptor
 */
@Singleton
public class VersionedClientWorldInterceptor {

  private final ClientPlayer clientPlayer;
  private final ClientWorld clientWorld;
  private final EntityMapper entityMapper;
  private final PassiveEntityMapper passiveEntityMapper;
  private final RemoteClientPlayer.Provider remoteClientPlayerEntityProvider;

  @Inject
  private VersionedClientWorldInterceptor(
          ClientPlayer clientPlayer,
          ClientWorld clientWorld,
          EntityMapper entityMapper,
          PassiveEntityMapper passiveEntityMapper,
          RemoteClientPlayer.Provider remoteClientPlayerEntityProvider) {
    this.clientPlayer = clientPlayer;
    this.clientWorld = clientWorld;
    this.entityMapper = entityMapper;
    this.passiveEntityMapper = passiveEntityMapper;
    this.remoteClientPlayerEntityProvider = remoteClientPlayerEntityProvider;
  }

  @Hook(
          className = "net.minecraft.client.world.ClientWorld",
          methodName = "addEntityImpl",
          parameters = {
                  @Type(reference = int.class),
                  @Type(reference = net.minecraft.entity.Entity.class)
          }
  )
  public void hookAddEntityImpl(@Named("args") Object[] args) {
    int entityId = (int) args[0];
    net.minecraft.entity.Entity entity = (net.minecraft.entity.Entity) args[1];

    if (entity instanceof PigEntity) {
      PigEntity pigEntity = (PigEntity) entity;

      this.clientWorld.getEntities().put(
              entityId,
              this.passiveEntityMapper.fromMinecraftPigEntity(pigEntity)
      );
    } else if (!(entity instanceof AbstractClientPlayerEntity || entity instanceof RemoteClientPlayerEntity)) {
      this.clientWorld.getEntities().put(
              entityId,
              this.entityMapper.fromMinecraftEntity(entity)
      );
    }

  }

  @Hook(
          className = "net.minecraft.client.world.ClientWorld",
          methodName = "removeEntityFromWorld",
          parameters = {
                  @Type(reference = int.class)
          }
  )
  public void hookRemoveEntityFromWorld(@Named("args") Object[] args) {
    int entityId = (int) args[0];

    Entity entity = this.clientWorld.getEntities().get(entityId);

    if (entity != null) {
      this.clientWorld.getEntities().remove(entityId, entity);
    }

  }

  @Hook(
          className = "net.minecraft.client.world.ClientWorld",
          methodName = "addPlayer",
          parameters = {
                  @Type(reference = int.class),
                  @Type(reference = AbstractClientPlayerEntity.class)
          }
  )
  public void hookAfterAddPlayer(@Named("args") Object[] args) {
    AbstractClientPlayerEntity playerEntity = (AbstractClientPlayerEntity) args[1];

    if (playerEntity instanceof ClientPlayerEntity) {
      this.clientWorld.addPlayer(this.clientPlayer);
    } else if (playerEntity instanceof RemoteClientPlayerEntity) {
      this.clientWorld.addPlayer(this.remoteClientPlayerEntityProvider.get(playerEntity));
    }

  }

  @Hook(
          className = "net.minecraft.client.world.ClientWorld",
          methodName = "removeEntity",
          parameters = {
                  @Type(reference = net.minecraft.entity.Entity.class)
          }
  )
  public void hookAfterRemoveEntity(@Named("args") Object[] args) {
    net.minecraft.entity.Entity entity = (net.minecraft.entity.Entity) args[0];

    if (entity instanceof AbstractClientPlayerEntity) {
      AbstractClientPlayerEntity playerEntity = (AbstractClientPlayerEntity) entity;
      this.clientWorld.removePlayer(playerEntity.getGameProfile().getId());
    }
  }
}
