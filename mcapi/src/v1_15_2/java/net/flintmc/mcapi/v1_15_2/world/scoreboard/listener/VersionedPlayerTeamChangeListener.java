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

package net.flintmc.mcapi.v1_15_2.world.scoreboard.listener;

import com.google.inject.Inject;
import java.util.Optional;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.internal.world.scoreboard.listener.PlayerTeamChangeListener;
import net.flintmc.mcapi.world.mapper.ScoreboardMapper;
import net.flintmc.mcapi.world.scoreboad.score.PlayerTeam;
import net.flintmc.mcapi.world.scoreboad.type.CollisionType;
import net.flintmc.mcapi.world.scoreboad.type.VisibleType;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team.CollisionRule;
import net.minecraft.scoreboard.Team.Visible;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

@Implement(value = PlayerTeamChangeListener.class, version = "1.15.2")
public class VersionedPlayerTeamChangeListener implements PlayerTeamChangeListener {

  private final ScoreboardMapper scoreboardMapper;
  private final MinecraftComponentMapper componentMapper;

  @Inject
  public VersionedPlayerTeamChangeListener(
      ScoreboardMapper scoreboardMapper, MinecraftComponentMapper componentMapper) {
    this.scoreboardMapper = scoreboardMapper;
    this.componentMapper = componentMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changeDisplayName(PlayerTeam playerTeam, ChatComponent displayName) {
    this.getPlayerTeam(playerTeam).ifPresent(team -> team.setDisplayName(
        (ITextComponent) this.componentMapper.toMinecraft(displayName)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changePrefix(PlayerTeam playerTeam, ChatComponent prefix) {
    this.getPlayerTeam(playerTeam).ifPresent(team -> team.setPrefix(
        (ITextComponent) this.componentMapper.toMinecraft(prefix)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changeSuffix(PlayerTeam playerTeam, ChatComponent suffix) {
    this.getPlayerTeam(playerTeam).ifPresent(team -> team.setSuffix(
        (ITextComponent) this.componentMapper.toMinecraft(suffix)));
  }


  @Override
  public void changeColor(PlayerTeam playerTeam, String colorName) {
    this.getPlayerTeam(playerTeam).ifPresent(team -> team.setColor(TextFormatting.valueOf(colorName)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changeColor(PlayerTeam playerTeam, ChatColor chatColor) {
    this.changeColor(playerTeam, chatColor.getLowerName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changeAllowFriendlyFire(PlayerTeam playerTeam, boolean friendlyFire) {
    this.getPlayerTeam(playerTeam).ifPresent(team -> team.setAllowFriendlyFire(friendlyFire));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changeSeeFriendlyInvisible(PlayerTeam playerTeam, boolean friendlyInvisible) {
    this.getPlayerTeam(playerTeam)
        .ifPresent(team -> team.setSeeFriendlyInvisiblesEnabled(friendlyInvisible));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changeNameTagVisibility(PlayerTeam playerTeam, VisibleType visibleType) {
    this.getPlayerTeam(playerTeam).ifPresent(team -> team.setNameTagVisibility(
        (Visible) this.scoreboardMapper.toMinecraftVisible(visibleType)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changeDeathMessageVisibility(PlayerTeam playerTeam, VisibleType visibleType) {
    this.getPlayerTeam(playerTeam).ifPresent(team -> team.setDeathMessageVisibility(
        (Visible) this.scoreboardMapper.toMinecraftVisible(visibleType)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changeCollisionType(PlayerTeam playerTeam, CollisionType collisionType) {
    this.getPlayerTeam(playerTeam).ifPresent(team -> team.setCollisionRule(
        (CollisionRule) this.scoreboardMapper.toMinecraftCollisionRule(collisionType)));
  }

  private Optional<ScorePlayerTeam> getPlayerTeam(PlayerTeam name) {
    Scoreboard scoreboard = Minecraft.getInstance().world.getScoreboard();

    return scoreboard == null ? Optional.empty()
        : Optional.ofNullable(scoreboard.getTeam(name.getName()));
  }
}
