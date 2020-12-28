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
import net.flintmc.mcapi.world.scoreboad.score.Criteria;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;

@Implement(Criteria.class)
public class DefaultCriteria implements Criteria {

  private final String name;
  private final boolean readOnly;
  private final RenderType renderType;

  @AssistedInject
  private DefaultCriteria(@Assisted("name") String name) {
    this(name, false, RenderType.INTEGER);
  }

  @AssistedInject
  private DefaultCriteria(
      @Assisted("name") String name,
      @Assisted("readOnly") boolean readOnly,
      @Assisted("renderType") RenderType renderType) {
    this.name = name;
    this.readOnly = readOnly;
    this.renderType = renderType;
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return this.name;
  }

  /** {@inheritDoc} */
  @Override
  public boolean readOnly() {
    return this.readOnly;
  }

  /** {@inheritDoc} */
  @Override
  public RenderType getRenderType() {
    return this.renderType;
  }
}
