package net.labyfy.internal.component.player.v1_15_2.world;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.player.ClientPlayer;
import net.labyfy.component.player.RemoteClientPlayer;
import net.labyfy.component.player.world.ClientWorld;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.Entity;

/**
 * The Object intercepts {@link net.minecraft.client.world.ClientWorld} to better cache the players on the world.
 */
@Singleton
@AutoLoad
public class VersionedClientWorldInterceptor {

  private final ClientWorld clientWorld;
  private final ClientPlayer.Factory factoryClientPlayer;
  private final RemoteClientPlayer.Factory<AbstractClientPlayerEntity> factoryRemoteClientPlayer;

  @Inject
  private VersionedClientWorldInterceptor(
          ClientWorld clientWorld,
          ClientPlayer.Factory factoryClientPlayer,
          RemoteClientPlayer.Factory factoryRemoteClientPlayer
  ) {
    this.clientWorld = clientWorld;
    this.factoryClientPlayer = factoryClientPlayer;
    this.factoryRemoteClientPlayer = factoryRemoteClientPlayer;
  }

  /**
   * Appends to the {@link net.minecraft.client.world.ClientWorld#addPlayer(int, AbstractClientPlayerEntity)} method.
   *
   * @param args The parameters of the hooked method
   */
/*  @Hook(
          executionTime = Hook.ExecutionTime.AFTER,
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
      this.clientWorld.addPlayer(this.factoryClientPlayer.create());
    } else if (playerEntity instanceof RemoteClientPlayerEntity) {
      this.clientWorld.addPlayer(this.factoryRemoteClientPlayer.create(playerEntity));
    }
  }
*/
  /**
   * Appends to the {@link net.minecraft.client.world.ClientWorld#removeEntity(Entity)} method.
   *
   * @param args The parameters of the hooked method
   */
 /* @Hook(
          executionTime = Hook.ExecutionTime.AFTER,
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
      this.clientWorld.removeIfPlayer(
              player -> player
                      .getGameProfile()
                      .getUniqueId()
                      .equals(
                              playerEntity.getGameProfile().getId()
                      )
      );
    }
  }
*/
}
