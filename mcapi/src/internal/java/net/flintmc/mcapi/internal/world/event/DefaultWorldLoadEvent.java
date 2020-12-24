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

package net.flintmc.mcapi.internal.world.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.event.WorldLoadEvent;

/** {@inheritDoc} */
@Implement(WorldLoadEvent.class)
public class DefaultWorldLoadEvent implements WorldLoadEvent {

  private final String worldName;
  private State state;
  private float processPercentage;

  @AssistedInject
  public DefaultWorldLoadEvent(
      @Assisted String worldName, @Assisted State state, @Assisted float processPercentage) {
    this.worldName = worldName;
    this.state = state;
    this.processPercentage = processPercentage;
  }

  /** {@inheritDoc} */
  @Override
  public String getWorldName() {
    return this.worldName;
  }

  /** {@inheritDoc} */
  @Override
  public State getState() {
    return this.state;
  }

  public void setType(State state) {
    this.state = state;
  }

  /** {@inheritDoc} */
  @Override
  public float getProcessPercentage() {
    return this.processPercentage;
  }

  public void setProcessPercentage(float processPercentage) {
    this.processPercentage = processPercentage;
  }
}
