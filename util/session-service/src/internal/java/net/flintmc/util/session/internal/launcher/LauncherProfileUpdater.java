package net.flintmc.util.session.internal.launcher;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.event.subscribe.PostSubscribe;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.util.session.SessionService;
import net.flintmc.util.session.event.SessionAccountLogInEvent;
import net.flintmc.util.session.event.SessionTokenRefreshEvent;
import net.flintmc.util.session.launcher.LauncherProfile;
import net.flintmc.util.session.launcher.LauncherProfileResolver;
import net.flintmc.util.session.launcher.LauncherProfiles;

import java.io.IOException;

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
    // update the token in the launcher_profiles.json on log in since this can also grant a new token
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
