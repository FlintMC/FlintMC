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

package net.flintmc.mcapi.v1_16_4.world.scoreboard.listener;

import com.google.inject.Inject;
import java.util.Optional;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.internal.world.scoreboard.listener.ScoreChangeListener;
import net.flintmc.mcapi.world.mapper.ScoreboardMapper;
import net.flintmc.mcapi.world.scoreboad.score.Score;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

@Implement(value = ScoreChangeListener.class, version = "1.15.2")
public class VersionedScoreChangeListener implements ScoreChangeListener {

  private final ScoreboardMapper scoreboardMapper;

  @Inject
  private VersionedScoreChangeListener(ScoreboardMapper scoreboardMapper) {
    this.scoreboardMapper = scoreboardMapper;
  }

  /** {@inheritDoc} */
  @Override
  public void changeScorePoints(Score score, int points) {
    this.getScore(score).ifPresent(s -> s.setScorePoints(points));
  }

  /** {@inheritDoc} */
  @Override
  public void changeLocked(Score score, boolean locked) {
    this.getScore(score).ifPresent(s -> s.setLocked(locked));
  }

  private Optional<net.minecraft.scoreboard.Score> getScore(Score score) {
    Scoreboard scoreboard = Minecraft.getInstance().world.getScoreboard();

    return scoreboard == null
        ? Optional.empty()
        : Optional.ofNullable(
            scoreboard.getOrCreateScore(
                score.getPlayerName(),
                (ScoreObjective) this.scoreboardMapper.toMinecraftObjective(score.getObjective())));
  }
}
