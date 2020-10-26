package net.labyfy.internal.component.session.launcher;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.session.launcher.LauncherProfile;

import java.util.UUID;

@Implement(LauncherProfile.class)
public class DefaultLauncherProfile implements LauncherProfile {

  private final String profileId;
  private final GameProfile[] profiles;

  private String accessToken;

  @AssistedInject
  private DefaultLauncherProfile(@Assisted("profileId") String profileId, @Assisted("accessToken") String accessToken,
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
