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

package net.flintmc.mcapi.internal.world.scoreboard.score;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.internal.world.scoreboard.listener.ObjectiveChangeListener;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;
import net.flintmc.mcapi.world.scoreboad.score.Criteria;
import net.flintmc.mcapi.world.scoreboad.score.Objective;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;

@Implement(Objective.class)
public class DefaultObjective implements Objective {

  private final Scoreboard scoreboard;
  private final ObjectiveChangeListener objectiveChangeListener;
  private final String name;
  private final Criteria criteria;
  private ChatComponent displayName;
  private RenderType renderType;

  @AssistedInject
  public DefaultObjective(
      Scoreboard scoreboard,
      ObjectiveChangeListener objectiveChangeListener,
      @Assisted("name") String name,
      @Assisted("displayName") ChatComponent displayName,
      @Assisted("criteria") Criteria criteria,
      @Assisted("renderType") RenderType renderType) {
    this.scoreboard = scoreboard;
    this.objectiveChangeListener = objectiveChangeListener;
    this.name = name;
    this.displayName = displayName;
    this.criteria = criteria;
    this.renderType = renderType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Scoreboard getScoreboard() {
    return this.scoreboard;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDisplayName(ChatComponent displayName) {
    this.displayName = displayName;
    this.objectiveChangeListener.changeDisplayName(this, displayName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Criteria getCriteria() {
    return this.criteria;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RenderType getRenderType() {
    return this.renderType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRenderType(RenderType renderType) {
    this.renderType = renderType;
    this.objectiveChangeListener.changeRenderType(this, renderType);
  }
}
