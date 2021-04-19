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

package net.flintmc.mcapi.internal.world.lan;

import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.world.lan.LanWorldOptions;

@Implement(LanWorldOptions.class)
public class DefaultLanWorldOptions implements LanWorldOptions {

  private GameMode mode = GameMode.SURVIVAL;
  private boolean allowCheats = false;
  private boolean showInfoMessage = true;

  @AssistedInject
  private DefaultLanWorldOptions() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LanWorldOptions mode(GameMode mode) {
    this.mode = mode;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GameMode mode() {
    return this.mode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LanWorldOptions allowCheats(boolean allowCheats) {
    this.allowCheats = allowCheats;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean allowCheats() {
    return this.allowCheats;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LanWorldOptions showInfoMessage(boolean showInfoMessage) {
    this.showInfoMessage = showInfoMessage;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean showInfoMessage() {
    return this.showInfoMessage;
  }
}
