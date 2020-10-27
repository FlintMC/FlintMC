package net.flintmc.util.session.internal.launcher;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.util.session.SessionService;
import net.flintmc.util.session.launcher.LauncherProfileResolver;
import net.flintmc.util.session.launcher.LauncherProfiles;
import net.flintmc.framework.tasks.Task;
import net.flintmc.framework.tasks.Tasks;

import java.io.IOException;

@Singleton
public class InitialLauncherProfileReader {

  private final LauncherProfileResolver resolver;
  private final SessionService sessionService;

  @Inject
  public InitialLauncherProfileReader(LauncherProfileResolver resolver, SessionService sessionService) {
    this.resolver = resolver;
    this.sessionService = sessionService;
  }

  @Task(Tasks.POST_MINECRAFT_INITIALIZE)
  public void readLauncherProfiles() throws IOException {
    // load the launcher_profiles.json from the launcher to get the initial clientToken which is necessary
    // to refresh the accessToken

    LauncherProfiles profiles = this.resolver.loadProfiles();
    if (profiles == null) {
      return;
    }

    this.sessionService.setClientToken(profiles.getClientToken());
  }

}
