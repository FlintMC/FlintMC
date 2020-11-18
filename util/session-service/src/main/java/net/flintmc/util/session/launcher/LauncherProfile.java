package net.flintmc.util.session.launcher;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.player.gameprofile.GameProfile;

import java.util.UUID;

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
   * @return The {@link GameProfile} with the given {@code uniqueId} or {@code null} if there is no
   *     {@link GameProfile} with the given {@code uniqueId} in {@link #getProfiles() the profiles}
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

  /** Factory for the {@link LauncherProfile}. */
  @AssistedFactory(LauncherProfile.class)
  interface Factory {

    /**
     * Creates an new {@link LauncherProfile} with the given parameters.
     *
     * @param profileId The non-null profileId of this profile
     * @param accessToken The non-null accessToken which should be used with the clientToken from
     *     {@link LauncherProfiles#getClientToken()} to authenticate with a mojang account
     * @param profiles The non-null array of non-null profiles
     * @return The new non-null {@link LauncherProfile}
     */
    LauncherProfile create(
        @Assisted("profileId") String profileId,
        @Assisted("accessToken") String accessToken,
        @Assisted("profiles") GameProfile[] profiles);
  }
}