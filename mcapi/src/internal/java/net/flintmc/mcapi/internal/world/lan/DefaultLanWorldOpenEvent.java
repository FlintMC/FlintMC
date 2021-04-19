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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.lan.LanWorldOpenEvent;
import net.flintmc.mcapi.world.lan.LanWorldOptions;

@Implement(LanWorldOpenEvent.class)
public class DefaultLanWorldOpenEvent implements LanWorldOpenEvent {

  private final boolean success;
  private int port;
  private LanWorldOptions options;
  private boolean cancelled;

  @AssistedInject
  private DefaultLanWorldOpenEvent(
      @Assisted("success") boolean success,
      @Assisted("port") int port,
      @Assisted("options") LanWorldOptions options) {
    this.success = success;
    this.port = port;
    this.options = options;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean wasSuccess() {
    return this.success;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPort() {
    return this.port;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPort(int port) {
    this.port = port;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LanWorldOptions getOptions() {
    return this.options;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setOptions(LanWorldOptions options) {
    this.options = options;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
