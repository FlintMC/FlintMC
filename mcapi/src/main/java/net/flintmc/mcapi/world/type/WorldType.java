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
 * <p><b>DEPRECATED</b> This is deprecated because the world types have changed in 1.16.x.
 */
// TODO: 30.12.2020 Need a new implementation
@Deprecated
public interface WorldType {

  /**
   * Retrieves the name of the world type.
   *
   * @return The world type's name.
   */
  String getName();

  /**
   * Retrieves the serialization identifier of the world type.
   *
   * @return The world type's serialization identifier.
   */
  String getSerializationIdentifier();

  /**
   * Retrieves the version of the world type.
   *
   * @return The world type's version.
   */
  int getVersion();

  /**
   * Whether the world type has a custom configuration.
   *
   * @return {@code true} if the world type has a custom configuration, otherwise {@code false}.
   */
  boolean hasCustomConfigurations();

  /**
   * Changes whether the world type has a custom configuration.
   *
   * @param customConfigurations {@code true} if the world type has a custom configuration,
   *                             otherwise {@code false}.
   */
  void setCustomConfigurations(boolean customConfigurations);

  /**
   * Whether the world type can be created.
   *
   * @return {@code true} if the world can be created.
   */
  boolean canBeCreated();

  /**
   * Changes whether the world type can be created.
   *
   * @param canBeCreated {@code true} if the world type can be created, otherwise {@code false}.
   */
  void setCanBeCreated(boolean canBeCreated);

  /**
   * Whether the world type is versioned.
   *
   * @return {@code true} if the world type is versioned, otherwise {@code false}.
   */
  boolean isVersioned();

  /**
   * Enables the versioned of the world type.
   */
  void enableVersioned();

  /**
   * Retrieves the identifier of the world type.
   *
   * @return The world type's identifier.
   */
  int getIdentifier();

  /**
   * Whether the world type has an information notice.
   *
   * @return {@code true} if the world type has an information notice, otherwise {@code false}.
   */
  boolean hasInfoNotice();

  /**
   * Enables the information notice of the world type.
   */
  void enabledInfoNotice();

  /**
   * A factory class for creating {@link WorldType}'s.
   */
  @AssistedFactory(WorldType.class)
  @Deprecated
  interface Factory {

    /**
     * Creates a new {@link WorldType} with the given parameters.
     *
     * @param identifier The identifier for the world type.
     * @param name       The name for the world type.
     * @return A created world type.
     */
    WorldType create(@Assisted("identifier") int identifier, @Assisted("name") String name);

    /**
     * Creates a new {@link WorldType} with the given parameters.
     *
     * @param identifier The identifier for the world type.
     * @param name       The name for the world type.
     * @param version    The version for the world type.
     * @return A created world type.
     */
    WorldType create(
        @Assisted("identifier") int identifier,
        @Assisted("name") String name,
        @Assisted("version") int version);

    /**
     * Creates a new {@link WorldType} with the given parameters.
     *
     * @param identifier           The identifier for the world type.
     * @param name                 The name for the world type.
     * @param serializedIdentifier The serialized identifier for the world type.
     * @param version              The version for the world type.
     * @return A created world type.
     */
    WorldType create(
        @Assisted("identifier") int identifier,
        @Assisted("name") String name,
        @Assisted("serializedIdentifier") String serializedIdentifier,
        @Assisted("version") int version);

    /**
     * Creates a new {@link WorldType} with the given parameters.
     *
     * @param identifier           The identifier for the world type.
     * @param name                 The name for the world type.
     * @param serializedIdentifier The serialized identifier for the world type.
     * @param version              The version for the world type.
     * @param canBeCreated         {@code true} if the world type can be created, otherwise {@code
     *                             false}.
     * @param versioned            {@code true} if the world type is versioned, otherwise {@code
     *                             false}.
     * @param hasInfoNotice        {@code true} if the world type has an information notice,
     *                             otherwise {@code false}.
     * @param customConfiguration  {@code true} if the world type has a custom configuration,
     *                             otherwise {@code false}.
     * @return A created world type.
     */
    WorldType create(
        @Assisted("identifier") int identifier,
        @Assisted("name") String name,
        @Assisted("serializedIdentifier") String serializedIdentifier,
        @Assisted("version") int version,
        @Assisted("canBeCreated") boolean canBeCreated,
        @Assisted("versioned") boolean versioned,
        @Assisted("hasInfoNotice") boolean hasInfoNotice,
        @Assisted("customConfiguration") boolean customConfiguration);
  }
}
