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

package net.flintmc.util.session.launcher;

import java.util.Collection;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.util.session.launcher.serializer.LauncherProfileSerializer;

/**
 * Represents the part of the launcher_profiles.json that contains the accounts, clientToken and
 * format which can be read/stored with the {@link LauncherProfileResolver}.
 */
public interface LauncherProfiles {

  /**
   * Retrieves the clientToken from the launcher_profiles.json which is used for authentication with
   * mojang.
   *
   * @return The non-null clientToken
   */
  String getClientToken();

  /**
   * Retrieves the version out of the launcher_profiles.json if a serializer is present for it.
   *
   * @return The version or {@link LauncherProfileResolver#getHighestSerializerVersion()} if no
   * serializer is registered with the version from the file
   */
  int getPreferredVersion();

  /**
   * Retrieves all profiles from the launcher_profiles.json that have been collected by the {@link
   * LauncherProfileSerializer}.
   *
   * @return The non-null collection of all non-null profiles from the file
   */
  Collection<LauncherProfile> getProfiles();

  /**
   * Factory for the {@link LauncherProfiles}.
   */
  @AssistedFactory(LauncherProfiles.class)
  interface Factory {

    /**
     * Creates new {@link LauncherProfiles} with the given parameters.
     *
     * @param clientToken      The non-null clientToken from the launcher_profiles.json
     * @param preferredVersion The version which should be used to serialize the new object
     * @param profiles         The non-null collection of all non-null profiles from the
     *                         launcher_profiles.json
     * @return The new non-null {@link LauncherProfiles}
     */
    LauncherProfiles create(
        @Assisted("clientToken") String clientToken,
        @Assisted("preferredVersion") int preferredVersion,
        @Assisted("profiles") Collection<LauncherProfile> profiles);
  }
}
