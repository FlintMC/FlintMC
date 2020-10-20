package net.labyfy.internal.component.session.launcher;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.session.SessionService;
import net.labyfy.component.session.launcher.LauncherProfileResolver;
import net.labyfy.component.session.launcher.LauncherProfiles;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;

import java.io.IOException;

@Singleton
@AutoLoad
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
