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

import java.util.UUID;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.player.gameprofile.GameProfile;

/**
 * Represents an account from the launcher_profiles.json.
 *
 * @see LauncherProfiles
 */
public interface LauncherProfile {

  /**
   * Retrieves a {@link GameProfile} from this profile with the given {@code uniqueId}.
   *
   * @param uniqueId The non-null uniqueId of the profile
   * @return The {@link GameProfile} with the given {@code uniqueId} or {@code null}, if there is no
   * {@link GameProfile} with the given {@code uniqueId} in {@link #getProfiles() the profiles}
   */
  GameProfile getProfile(UUID uniqueId);

  /**
   * Retrieves the ID of this profile which is unique per {@link LauncherProfiles}.
   *
   * @return The non-null id of this profile
   */
  String getProfileId();

  /**
   * Retrieves all game profiles from this profile.
   *
   * @return The non-null array of non-null profiles which may not be modified
   */
  GameProfile[] getProfiles();

  /**
   * Retrieves the non-null accessToken which should be used with the clientToken from {@link
   * LauncherProfiles#getClientToken()} to authenticate with a mojang account.
   *
   * @return The non-null accessToken from this profile
   */
  String getAccessToken();

  /**
   * Sets the accessToken in this profile which should be used with the clientToken from {@link
   * LauncherProfiles#getClientToken()} to authenticate with a mojang account.
   *
   * @param accessToken The non-null accessToken for this profile
   */
  void setAccessToken(String accessToken);

  /**
   * Factory for the {@link LauncherProfile}.
   */
  @AssistedFactory(LauncherProfile.class)
  interface Factory {

    /**
     * Creates an new {@link LauncherProfile} with the given parameters.
     *
     * @param profileId   The non-null profileId of this profile
     * @param accessToken The non-null accessToken which should be used with the clientToken from
     *                    {@link LauncherProfiles#getClientToken()} to authenticate with a mojang
     *                    account
     * @param profiles    The non-null array of non-null profiles
     * @return The new non-null {@link LauncherProfile}
     */
    LauncherProfile create(
        @Assisted("profileId") String profileId,
        @Assisted("accessToken") String accessToken,
        @Assisted("profiles") GameProfile[] profiles);
  }
}
