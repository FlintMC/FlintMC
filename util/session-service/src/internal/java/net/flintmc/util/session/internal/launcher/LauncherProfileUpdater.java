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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import net.flintmc.framework.eventbus.event.subscribe.PostSubscribe;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.util.session.SessionService;
import net.flintmc.util.session.event.SessionAccountLogInEvent;
import net.flintmc.util.session.event.SessionTokenRefreshEvent;
import net.flintmc.util.session.launcher.LauncherProfile;
import net.flintmc.util.session.launcher.LauncherProfileResolver;
import net.flintmc.util.session.launcher.LauncherProfiles;

@Singleton
public class LauncherProfileUpdater {

  private final LauncherProfileResolver resolver;
  private final SessionService sessionService;

  @Inject
  public LauncherProfileUpdater(LauncherProfileResolver resolver, SessionService sessionService) {
    this.resolver = resolver;
    this.sessionService = sessionService;
  }

  @PostSubscribe
  public void updateLauncherProfiles(SessionAccountLogInEvent event) throws IOException {
    // update the token in the launcher_profiles.json on log in since this can also grant a new
    // token
    this.updateAccessToken(this.sessionService.getAccessToken());
  }

  @PostSubscribe
  public void updateLauncherProfiles(SessionTokenRefreshEvent event) throws IOException {
    // update the token in the launcher_profiles.json on refresh
    this.updateAccessToken(event.getNewAccessToken());
  }

  private void updateAccessToken(String newAccessToken) throws IOException {
    GameProfile gameProfile = this.sessionService.getProfile();
    if (gameProfile == null) {
      return;
    }

    LauncherProfiles profiles = this.resolver.loadProfiles();
    if (profiles == null) {
      return;
    }

    boolean updated = false;
    for (LauncherProfile profile : profiles.getProfiles()) {
      if (profile.getProfile(gameProfile.getUniqueId()) == null) {
        continue;
      }

      profile.setAccessToken(newAccessToken);
      updated = true;
    }

    if (!updated) {
      return;
    }

    this.resolver.storeProfiles(profiles);
  }
}
