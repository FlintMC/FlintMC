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

package net.flintmc.mcapi.world.type;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents the type of the world.
 *
 * @see WorldTypeRegistry
 */
public interface WorldType {

  /**
   * Retrieves the name of the world type.
   *
   * @return The world type's name.
   */
  String getName();

  /**
   * Whether the world type has a custom configuration.
   *
   * @return {@code true} if the world type has a custom configuration, otherwise {@code false}.
   */
  boolean hasCustomConfigurations();

  /**
   * Whether the world type can be created.
   *
   * @return {@code true} if the world can be created.
   */
  boolean canBeCreated();

  /**
   * A factory class for creating {@link WorldType}'s.
   */
  @AssistedFactory(WorldType.class)
  interface Factory {

    /**
     * Creates a new {@link WorldType} with the given parameters.
     *
     * @param name                The name for the world type.
     * @param canBeCreated        {@code true} if the world type can be created, otherwise {@code
     *                            false}.
     * @param customConfiguration {@code true} if the world type has a custom configuration,
     *                            otherwise {@code false}.
     * @return A created world type.
     */
    WorldType create(
        @Assisted("name") String name,
        @Assisted("canBeCreated") boolean canBeCreated,
        @Assisted("customConfiguration") boolean customConfiguration);
  }
}
