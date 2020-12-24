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

package net.flintmc.mcapi.internal.chat.component;

import net.flintmc.mcapi.chat.component.ScoreComponent;

public class DefaultScoreComponent extends DefaultChatComponent implements ScoreComponent {

  private String name;
  private String objective;

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public void name(String name) {
    this.name = name;
  }

  @Override
  public String objective() {
    return this.objective;
  }

  @Override
  public void objective(String objective) {
    this.objective = objective;
  }

  @Override
  public String getUnformattedText() {
    return "null"; // TODO get text out of the scoreboard
  }

  @Override
  protected DefaultChatComponent createCopy() {
    DefaultScoreComponent component = new DefaultScoreComponent();
    component.name = this.name;
    component.objective = this.objective;
    return component;
  }

  @Override
  protected boolean isSpecificEmpty() {
    return this.name == null && this.objective == null;
  }
}
