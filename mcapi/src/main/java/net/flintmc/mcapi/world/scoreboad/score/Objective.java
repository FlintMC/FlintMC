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

package net.flintmc.mcapi.world.scoreboad.score;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;

/**
 * Represents a Minecraft score objective
 */
public interface Objective {

  /**
   * Retrieves the scoreboard of this objective.
   *
   * @return The objective's scoreboard.
   */
  Scoreboard getScoreboard();

  /**
   * Retrieves the registry name of this objective.
   *
   * @return The registry name of this objective
   */
  String getName();

  /**
   * Retrieves the display name of this objective
   *
   * @return The display name of this objective
   */
  ChatComponent getDisplayName();

  /**
   * Changes the display name of this objective.
   *
   * @param displayName The new display name.
   */
  void setDisplayName(ChatComponent displayName);

  /**
   * Retrieves the criteria of this objective
   *
   * @return The criteria of this objective
   */
  Criteria getCriteria();

  /**
   * Retrieves the render type of this objective
   *
   * @return The render type of this objective
   */
  RenderType getRenderType();

  /**
   * Changes the render type of this objective.
   *
   * @param renderType The new render type.
   */
  void setRenderType(RenderType renderType);

  /**
   * A factory class for {@link Objective}
   */
  @AssistedFactory(Objective.class)
  interface Factory {

    /**
     * Creates a new {@link Objective} with the given parameters.
     *
     * @param name        The registry name for this objective.
     * @param displayName The name that is displayed.
     * @param criteria    The criteria for this objective.
     * @param type        The render type for this objective.
     * @return A created objective.
     */
    Objective create(
        @Assisted("name") String name,
        @Assisted("displayName") ChatComponent displayName,
        @Assisted("criteria") Criteria criteria,
        @Assisted("renderType") RenderType type);
  }
}
