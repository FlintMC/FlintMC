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

package net.flintmc.mcapi.v1_15_2.world.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.chat.format.ChatFormat;
import net.flintmc.mcapi.world.mapper.ScoreboardMapper;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;
import net.flintmc.mcapi.world.scoreboad.score.Criterias;
import net.flintmc.mcapi.world.scoreboad.score.Objective;
import net.flintmc.mcapi.world.scoreboad.score.PlayerTeam;
import net.flintmc.mcapi.world.scoreboad.score.Score;
import net.flintmc.mcapi.world.scoreboad.type.CollisionType;
import net.flintmc.mcapi.world.scoreboad.type.VisibleType;
import net.flintmc.transform.hook.Hook;
import net.minecraft.network.play.server.SDisplayObjectivePacket;
import net.minecraft.network.play.server.SScoreboardObjectivePacket;
import net.minecraft.network.play.server.STeamsPacket;
import net.minecraft.network.play.server.SUpdateScorePacket;

/**
 * 1.15.2 implementation of the scoreboard interceptor.
 */
@Singleton
public class VersionedScoreboardInterceptor {

  private final Scoreboard scoreboard;
  private final ScoreboardMapper scoreboardMapper;

  private final MinecraftComponentMapper componentMapper;

  @Inject
  public VersionedScoreboardInterceptor(
      Scoreboard scoreboard,
      ScoreboardMapper scoreboardMapper,
      MinecraftComponentMapper componentMapper) {
    this.scoreboard = scoreboard;
    this.scoreboardMapper = scoreboardMapper;
    this.componentMapper = componentMapper;
  }

  @Hook(
      className = "net.minecraft.client.network.play.ClientPlayNetHandler",
      methodName = "handleScoreboardObjective",
      parameters = {@Type(reference = SScoreboardObjectivePacket.class)},
      version = "1.15.2")
  public void hookHandleScoreboardObjective(@Named("args") Object[] args) {
    SScoreboardObjectivePacket packet = (SScoreboardObjectivePacket) args[0];

    String objectiveName = packet.getObjectiveName();
    int action = packet.getAction();

    if (action == 0) {
      this.scoreboard.addObjective(
          objectiveName,
          Criterias.DUMMY,
          this.componentMapper.fromMinecraft(packet.getDisplayName()),
          this.scoreboardMapper.fromMinecraftRenderType(packet.getRenderType()));
    } else if (this.scoreboard.hasObjective(objectiveName)) {
      Objective objective = this.scoreboard.getObjective(objectiveName);

      if (action == 1) {
        this.scoreboard.removeObjective(objective);
      } else if (action == 2) {
        objective.setRenderType(
            this.scoreboardMapper.fromMinecraftRenderType(packet.getRenderType()));
        objective.setDisplayName(this.componentMapper.fromMinecraft(packet.getDisplayName()));
      }
    }
  }

  @Hook(
      className = "net.minecraft.client.network.play.ClientPlayNetHandler",
      methodName = "handleUpdateScore",
      parameters = {@Type(reference = SUpdateScorePacket.class)},
      version = "1.15.2")
  public void hookHandleUpdateScore(@Named("args") Object[] args) {
    SUpdateScorePacket packet = (SUpdateScorePacket) args[0];

    String objectiveName = packet.getObjectiveName();

    switch (packet.getAction()) {
      case CHANGE:
        Objective objective = this.scoreboard.getObjective(objectiveName);
        Score score = this.scoreboard.getOrCreateScore(packet.getPlayerName(), objective);
        score.setScorePoints(packet.getScoreValue());
        break;
      case REMOVE:
        this.scoreboard.removeObjectiveFromEntity(
            packet.getPlayerName(), this.scoreboard.getObjective(objectiveName));
        break;
    }
  }

  @Hook(
      className = "net.minecraft.client.network.play.ClientPlayNetHandler",
      methodName = "handleDisplayObjective",
      parameters = {@Type(reference = SDisplayObjectivePacket.class)},
      version = "1.15.2")
  public void hookHandleDisplayObjective(@Named("args") Object[] args) {
    SDisplayObjectivePacket packet = (SDisplayObjectivePacket) args[0];
    String name = packet.getName();

    Objective objective = name == null ? null : this.scoreboard.getObjective(name);
    this.scoreboard.setObjectiveInDisplaySlot(packet.getPosition(), objective);
  }

  @Hook(
      className = "net.minecraft.client.network.play.ClientPlayNetHandler",
      methodName = "handleTeams",
      parameters = {@Type(reference = STeamsPacket.class)},
  version = "1.15.2")
  public void hookHandleTeams(@Named("args") Object[] args) {
    STeamsPacket packet = (STeamsPacket) args[0];

    int action = packet.getAction();
    PlayerTeam playerTeam;

    playerTeam =
        action == 0
            ? this.scoreboard.createTeam(packet.getName())
            : this.scoreboard.getTeam(packet.getName());

    if (action == 0 || action == 2) {
      playerTeam.setDisplayName(this.componentMapper.fromMinecraft(packet.getDisplayName()));


      String friendlyName = packet.getColor().getFriendlyName();
      ChatColor color = ChatColor.getByName(friendlyName.toUpperCase());

      if (friendlyName.equalsIgnoreCase("reset")) {
        color = ChatColor.WHITE;
      }

      if(color != null) {
        playerTeam.setColor(color);
      } else {
        ChatFormat chatFormat = ChatFormat.getByName(friendlyName);

        if(chatFormat != null) {
          playerTeam.setChatFormat(chatFormat);
        }
      }

      playerTeam.setFriendlyFlags(packet.getFriendlyFlags());

      VisibleType teamVisibility = VisibleType.getByName(packet.getNameTagVisibility());

      if (teamVisibility != null) {
        playerTeam.setNameTagVisibility(teamVisibility);
      }

      CollisionType collisionType = CollisionType.getByName(packet.getCollisionRule());

      if (collisionType != null) {
        playerTeam.setCollisionType(collisionType);
      }

      playerTeam.setPrefix(this.componentMapper.fromMinecraft(packet.getPrefix()));
      playerTeam.setSuffix(this.componentMapper.fromMinecraft(packet.getSuffix()));
    }

    if (action == 0 || action == 3) {
      for (String player : packet.getPlayers()) {
        this.scoreboard.addPlayerToTeam(player, playerTeam);
      }
    }

    if (action == 4) {
      for (String player : packet.getPlayers()) {
        this.scoreboard.removePlayerFromTeam(player, playerTeam);
      }
    }

    // Removes a team from the scoreboard
    if (action == 1) {
      this.scoreboard.removeTeam(playerTeam);
    }
  }
}
