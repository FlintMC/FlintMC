package net.labyfy.internal.component.player.v1_15_2.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mojang.authlib.GameProfile;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.network.NetworkPlayerInfoRegistry;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.minecraft.network.play.server.SPlayerListItemPacket;

@Singleton
public class VersionedClientPlayNetHandlerInterceptor {

  private final NetworkPlayerInfoRegistry networkPlayerInfoRegistry;
  private final NetworkPlayerInfo.Factory networkPlayerInfoFactory;
  private final GameProfileSerializer<GameProfile> gameProfileGameProfileSerializer;

  @Inject
  private VersionedClientPlayNetHandlerInterceptor(
          NetworkPlayerInfoRegistry networkPlayerInfoRegistry,
          NetworkPlayerInfo.Factory networkPlayerInfoFactory,
          GameProfileSerializer<GameProfile> gameProfileGameProfileSerializer
  ) {
    this.networkPlayerInfoRegistry = networkPlayerInfoRegistry;
    this.networkPlayerInfoFactory = networkPlayerInfoFactory;
    this.gameProfileGameProfileSerializer = gameProfileGameProfileSerializer;
  }

  @Hook(
          className = "net.minecraft.client.network.play.ClientPlayNetHandler",
          methodName = "handlePlayerListItem",
          parameters = {
                  @Type(reference = SPlayerListItemPacket.class)
          }
  )
  public void hookHandlePlayerListItem(@Named("args") Object[] args) {
    SPlayerListItemPacket packet = (SPlayerListItemPacket) args[0];

    for (SPlayerListItemPacket.AddPlayerData entry : packet.getEntries()) {

      if (packet.getAction() == SPlayerListItemPacket.Action.REMOVE_PLAYER) {
        this.networkPlayerInfoRegistry.getPlayerInfoMap().remove(entry.getProfile().getId());
      } else if (packet.getAction() == SPlayerListItemPacket.Action.ADD_PLAYER) {
        this.networkPlayerInfoRegistry.getPlayerInfoMap().put(
                entry.getProfile().getId(),
                this.networkPlayerInfoFactory.create(
                        this.gameProfileGameProfileSerializer.deserialize(entry.getProfile())
                )
        );

      }

    }

  }
}
