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

package net.flintmc.util.session.launcher.serializer;

import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.Map;
import net.flintmc.util.session.launcher.LauncherProfile;

/**
 * Serializer for the {@link LauncherProfile}.
 *
 * @see ProfileSerializerVersion
 */
public interface LauncherProfileSerializer {

  /**
   * Updates the given json {@code authData} with the given {@code profiles}. If a profile is not
   * already added to the {@code authData} - which is usually identified by the {@link
   * LauncherProfile#getProfileId() profileId}, it will add the profile to the {@code authData},
   * otherwise the profile will just be added. If a profile is defined in the {@code authData} but
   * not in the {@code profiles}, it will be ignored (it will NOT be removed).
   *
   * @param profiles The non-null collection of profiles to be updated in the authData
   * @param authData The non-null authData to be filled with the profiles
   */
  void updateAuthData(Collection<LauncherProfile> profiles, JsonObject authData);

  /**
   * Reads the profiles from the given json {@code authData}.
   *
   * @param authData The non-null authData object from the launcher profiles
   * @return The new non-null map of profiles from the {@code authData} object
   */
  Map<String, LauncherProfile> readProfiles(JsonObject authData);
}
