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
import net.flintmc.mcapi.chat.component.ChatComponent;

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
   * Retrieves the display name of this world type which is usually a translation component.
   *
   * @return The non-null display name component of this world type
   */
  ChatComponent getDisplayName();

  /**
   * Retrieves whether the world type has a custom configuration which is for example the flat type
   * with its configurable flat layers.
   *
   * @return {@code true} if the world type has a custom configuration, otherwise {@code false}.
   */
  boolean hasCustomConfigurations();

  /**
   * Retrieves whether the world type can be created. For example types like the debug type of
   * Minecraft cannot be created.
   *
   * @return {@code true} if the world can be created, {@code false} otherwise
   */
  boolean canBeCreated();

  /**
   * Retrieves whether ths world type can be created when pressing shift on the selection. For
   * example types like the debug type of Minecraft need this.
   *
   * @return {@code true} if this world type can be created with shift, {@code false} otherwise
   */
  boolean canBeCreatedWithShift();

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
        @Assisted("displayName") ChatComponent displayName,
        @Assisted("canBeCreated") boolean canBeCreated,
        @Assisted("canBeCreatedWithShift") boolean canBeCreatedWithShift,
        @Assisted("customConfiguration") boolean customConfiguration);
  }
}
