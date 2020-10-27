package net.flintmc.util.session.launcher.serializer;

import com.google.gson.JsonObject;
import net.flintmc.util.session.launcher.LauncherProfile;

import java.util.Collection;
import java.util.Map;

/**
 * Serializer for the {@link LauncherProfile}.
 *
 * @see ProfileSerializerVersion
 */
public interface LauncherProfileSerializer {

  /**
   * Updates the given json {@code authData} with the given {@code profiles}. If a profile is not already added to the
   * {@code authData} - which is usually identified by the {@link LauncherProfile#getProfileId() profileId}, it will add
   * the profile to the {@code authData}, otherwise the profile will just be added. If a profile is defined in the
   * {@code authData} but not in the {@code profiles}, it will be ignored (it will NOT be removed).
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
