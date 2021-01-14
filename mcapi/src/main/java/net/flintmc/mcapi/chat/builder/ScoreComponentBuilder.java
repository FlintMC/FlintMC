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

package net.flintmc.mcapi.chat.builder;

import net.flintmc.mcapi.chat.component.ScoreComponent;

/**
 * Builder for {@link ScoreComponent}s.
 */
public interface ScoreComponentBuilder extends ComponentBuilder<ScoreComponentBuilder> {

  /**
   * Sets the name of the current component. The client will display the value out of the scoreboard
   * where this key is the line that is displayed next to the value.
   *
   * @param name The non-null key for the scoreboard value
   * @return this
   */
  ScoreComponentBuilder name(String name);

  /**
   * Retrieves the name of the current component.
   *
   * @return The name of the current component in the scoreboard or {@code null}, if no name has been
   * provided
   * @see #name(String)
   */
  String name();

  /**
   * Sets the objective of the current component. The client will use this to get the objective
   * containing the value to display.
   *
   * @param objective The non-null name of the objective
   * @return this
   */
  ScoreComponentBuilder objective(String objective);

  /**
   * Retrieves the objective of the current component.
   *
   * @return The objective of the current component in the scoreboard or {@code null}, if no
   * objective has been provided
   * @see #objective(String)
   */
  String objective();
}
