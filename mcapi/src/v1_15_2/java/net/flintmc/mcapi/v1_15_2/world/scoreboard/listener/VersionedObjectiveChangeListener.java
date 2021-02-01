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
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.internal.world.scoreboard.listener.ObjectiveChangeListener;
import net.flintmc.mcapi.world.mapper.ScoreboardMapper;
import net.flintmc.mcapi.world.scoreboad.score.Objective;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.ITextComponent;

import java.util.Optional;

@Implement(value = ObjectiveChangeListener.class, version = "1.15.2")
public class VersionedObjectiveChangeListener implements ObjectiveChangeListener {

  private final MinecraftComponentMapper componentMapper;
  private final ScoreboardMapper scoreboardMapper;

  @Inject
  public VersionedObjectiveChangeListener(
      MinecraftComponentMapper componentMapper, ScoreboardMapper scoreboardMapper) {
    this.componentMapper = componentMapper;
    this.scoreboardMapper = scoreboardMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changeDisplayName(Objective objective, ChatComponent displayName) {
    this.getObjective(objective)
        .ifPresent(
            scoreObjective -> scoreObjective.setDisplayName(
                (ITextComponent) this.componentMapper.toMinecraft(displayName)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changeRenderType(Objective objective, RenderType renderType) {
    this.getObjective(objective)
        .ifPresent(
            scoreObjective -> scoreObjective.setRenderType(
                (ScoreCriteria.RenderType)
                    this.scoreboardMapper.toMinecraftRenderType(renderType)));
  }

  private Optional<ScoreObjective> getObjective(Objective objective) {
    Scoreboard scoreboard = Minecraft.getInstance().world.getScoreboard();

    return scoreboard == null
        ? Optional.empty()
        : Optional.of(scoreboard.getObjective(objective.getName()));
  }
}
