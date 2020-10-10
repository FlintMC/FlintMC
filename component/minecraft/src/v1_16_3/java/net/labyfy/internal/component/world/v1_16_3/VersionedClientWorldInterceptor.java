package net.labyfy.internal.component.world.v1_16_3;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.player.ClientPlayer;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.component.world.ClientWorld;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.Entity;

/**
 * 1.16.3 implementation of the client world interceptor
 */
@Singleton
public class VersionedClientWorldInterceptor {

  private final ClientPlayer clientPlayer;
  private final ClientWorld clientWorld;

  @Inject
  private VersionedClientWorldInterceptor(
          ClientPlayer clientPlayer,
          ClientWorld clientWorld
  ) {
    this.clientPlayer = clientPlayer;
    this.clientWorld = clientWorld;
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
      // TODO: 21.09.2020 Wait for merge (#135-implement-1-16)
      this.clientWorld.addPlayer(null);
    }

  }

  @Hook(
          className = "net.minecraft.client.world.ClientWorld",
          methodName = "removeEntity",
          parameters = {
                  @Type(reference = Entity.class)
          }
  )
  public void hookAfterRemoveEntity(@Named("args") Object[] args) {
    Entity entity = (Entity) args[0];

    if (entity instanceof AbstractClientPlayerEntity) {
      AbstractClientPlayerEntity playerEntity = (AbstractClientPlayerEntity) entity;
      this.clientWorld.removePlayer(playerEntity.getGameProfile().getId());
    }
  }
}
