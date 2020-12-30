/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.v1_16_4.player.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.mcapi.player.event.PlayerInfoEvent;
import net.flintmc.mcapi.player.network.NetworkPlayerInfo;
import net.flintmc.mcapi.player.network.NetworkPlayerInfoRegistry;
import net.flintmc.mcapi.player.serializer.gameprofile.GameProfileSerializer;
import net.flintmc.mcapi.server.event.PacketEvent;
import net.minecraft.network.play.server.SPlayerListItemPacket;

@Singleton
public class VersionedPlayerInfoInterceptor {

  private final EventBus eventBus;
  private final PlayerInfoEvent.Factory eventFactory;
  private final NetworkPlayerInfoRegistry registry;
  private final NetworkPlayerInfo.Factory networkPlayerInfoFactory;
  private final GameProfileSerializer<GameProfile> gameProfileSerializer;

  @Inject
  private VersionedPlayerInfoInterceptor(
      EventBus eventBus,
      PlayerInfoEvent.Factory eventFactory,
      NetworkPlayerInfoRegistry registry,
      NetworkPlayerInfo.Factory networkPlayerInfoFactory,
      GameProfileSerializer gameProfileSerializer) {
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
    this.registry = registry;
    this.networkPlayerInfoFactory = networkPlayerInfoFactory;
    this.gameProfileSerializer = gameProfileSerializer;
  }

  @Subscribe(version = "1.16.4")
  public void handlePlayerList(PacketEvent packetEvent) {
    if (!(packetEvent.getPacket() instanceof SPlayerListItemPacket)) {
      return;
    }

    SPlayerListItemPacket packet = (SPlayerListItemPacket) packetEvent.getPacket();

    for (SPlayerListItemPacket.AddPlayerData data : packet.getEntries()) {
      GameProfile profile = data.getProfile();
      NetworkPlayerInfo info;

      if (packet.getAction() == SPlayerListItemPacket.Action.ADD_PLAYER) {
        info =
            this.networkPlayerInfoFactory.create(
                this.gameProfileSerializer.deserialize(data.getProfile()));
      } else {
        info = this.registry.getPlayerInfo(profile.getId());
      }

      if (info == null) {
        continue;
      }

      UUID uniqueId = info.getGameProfile().getUniqueId();

      PlayerInfoEvent event;

      switch (packet.getAction()) {
        case ADD_PLAYER:
          this.registry.getPlayerInfoMap().put(uniqueId, info);
          event = this.eventFactory.create(PlayerInfoEvent.Type.ADD, info);
          break;
        case UPDATE_GAME_MODE:
          event = this.eventFactory.create(PlayerInfoEvent.Type.UPDATE_GAME_MODE, info);
          break;
        case UPDATE_LATENCY:
          event = this.eventFactory.create(PlayerInfoEvent.Type.UPDATE_PING, info);
          break;
        case UPDATE_DISPLAY_NAME:
          event = this.eventFactory.create(PlayerInfoEvent.Type.UPDATE_DISPLAY_NAME, info);
          break;
        case REMOVE_PLAYER:
          this.registry.getPlayerInfoMap().remove(data.getProfile().getId());
          event = this.eventFactory.create(PlayerInfoEvent.Type.REMOVE, info);
          break;

        default:
          throw new IllegalStateException("Unexpected value: " + packet.getAction());
      }

      this.eventBus.fireEvent(event, Subscribe.Phase.POST);
    }
  }
}
