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

package net.flintmc.util.session.internal.launcher;

import java.util.UUID;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.util.session.launcher.LauncherProfile;

@Implement(LauncherProfile.class)
public class DefaultLauncherProfile implements LauncherProfile {

  private final String profileId;
  private final GameProfile[] profiles;

  private String accessToken;

  @AssistedInject
  private DefaultLauncherProfile(
      @Assisted("profileId") String profileId,
      @Assisted("accessToken") String accessToken,
      @Assisted("profiles") GameProfile[] profiles) {
    this.profileId = profileId;
    this.accessToken = accessToken;
    this.profiles = profiles;
  }

  @Override
  public GameProfile getProfile(UUID uniqueId) {
    for (GameProfile gameProfile : this.profiles) {
      if (gameProfile.getUniqueId().equals(uniqueId)) {
        return gameProfile;
      }
    }
    return null;
  }

  @Override
  public String getProfileId() {
    return this.profileId;
  }

  @Override
  public GameProfile[] getProfiles() {
    return this.profiles;
  }

  @Override
  public String getAccessToken() {
    return this.accessToken;
  }

  @Override
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
}
