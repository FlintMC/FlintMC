package net.labyfy.internal.component.player.v1_15_2.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mojang.authlib.GameProfile;
import net.labyfy.component.entity.mapper.EntityBaseMapper;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.network.NetworkPlayerInfoRegistry;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.minecraft.network.play.server.SPlayerListItemPacket;

@Singleton
@AutoLoad
public class VersionedClientPlayNetHandlerInterceptor {

  private final NetworkPlayerInfoRegistry networkPlayerInfoRegistry;
  private final NetworkPlayerInfo.Factory networkPlayerInfoFactory;
  private final GameProfileSerializer<GameProfile> gameProfileGameProfileSerializer;
  private final EntityBaseMapper entityBaseMapper;
  private final Scoreboard scoreboard;

  @Inject
  private VersionedClientPlayNetHandlerInterceptor(
          NetworkPlayerInfoRegistry networkPlayerInfoRegistry,
          NetworkPlayerInfo.Factory networkPlayerInfoFactory,
          GameProfileSerializer<GameProfile> gameProfileGameProfileSerializer,
          EntityBaseMapper entityBaseMapper,
          Scoreboard scoreboard
  ) {
    this.networkPlayerInfoRegistry = networkPlayerInfoRegistry;
    this.networkPlayerInfoFactory = networkPlayerInfoFactory;
    this.gameProfileGameProfileSerializer = gameProfileGameProfileSerializer;
    this.entityBaseMapper = entityBaseMapper;
    this.scoreboard = scoreboard;
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
                        this.gameProfileGameProfileSerializer.deserialize(entry.getProfile()),
                        this.scoreboard,
                        this.entityBaseMapper
                )
        );

      }
    }

  }
}
