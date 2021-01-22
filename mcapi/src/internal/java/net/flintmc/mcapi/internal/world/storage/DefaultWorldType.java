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
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.world.type.WorldType;

/**
 * Default implementation of the {@link WorldType}.
 */
@Implement(WorldType.class)
public class DefaultWorldType implements WorldType {

  private final String name;
  private final ChatComponent displayName;
  private final boolean canBeCreated;
  private final boolean canBeCreatedWithShift;
  private final boolean customConfiguration;

  @AssistedInject
  private DefaultWorldType(
      @Assisted("name") String name,
      @Assisted("displayName") ChatComponent displayName,
      @Assisted("canBeCreated") boolean canBeCreated,
      @Assisted("canBeCreatedWithShift") boolean canBeCreatedWithShift,
      @Assisted("customConfiguration") boolean customConfiguration) {
    this.name = name;
    this.displayName = displayName;
    this.canBeCreated = canBeCreated;
    this.canBeCreatedWithShift = canBeCreatedWithShift;
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
  public ChatComponent getDisplayName() {
    return this.displayName;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeCreatedWithShift() {
    return this.canBeCreatedWithShift;
  }
}
