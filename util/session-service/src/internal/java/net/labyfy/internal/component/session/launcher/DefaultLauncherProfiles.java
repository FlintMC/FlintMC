package net.labyfy.internal.component.session.launcher;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.session.launcher.LauncherProfile;
import net.labyfy.component.session.launcher.LauncherProfiles;

import java.util.Collection;

@Implement(LauncherProfiles.class)
public class DefaultLauncherProfiles implements LauncherProfiles {

  private final String clientToken;
  private final int preferredVersion;
  private final Collection<LauncherProfile> profiles;

  @AssistedInject
  private DefaultLauncherProfiles(@Assisted("clientToken") String clientToken, @Assisted("preferredVersion") int preferredVersion,
                                 @Assisted("profiles") Collection<LauncherProfile> profiles) {
    this.clientToken = clientToken;
    this.preferredVersion = preferredVersion;
    this.profiles = profiles;
  }

  @Override
  public String getClientToken() {
    return this.clientToken;
  }

  @Override
  public int getPreferredVersion() {
    return this.preferredVersion;
  }

  @Override
  public Collection<LauncherProfile> getProfiles() {
    return this.profiles;
  }
}
