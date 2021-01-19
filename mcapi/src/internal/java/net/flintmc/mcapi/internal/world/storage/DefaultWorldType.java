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

package net.flintmc.mcapi.internal.world.storage;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.type.WorldType;

/**
 * Default implementation of the {@link WorldType}.
 */
@Implement(WorldType.class)
public class DefaultWorldType implements WorldType {

  private final String name;
  private final boolean canBeCreated;
  private final boolean customConfiguration;

  @AssistedInject
  private DefaultWorldType(
      @Assisted("name") String name,
      @Assisted("canBeCreated") boolean canBeCreated,
      @Assisted("customConfiguration") boolean customConfiguration) {
    this.name = name;
    this.canBeCreated = canBeCreated;
    this.customConfiguration = customConfiguration;
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
  public boolean hasCustomConfigurations() {
    return this.customConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeCreated() {
    return this.canBeCreated;
  }
}
