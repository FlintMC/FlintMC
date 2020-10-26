package net.labyfy.component.session.launcher;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.Collection;

/**
 * Represents the part of the launcher_profiles.json that contains the accounts, clientToken and format which can be
 * read/stored with the {@link LauncherProfileResolver}.
 */
public interface LauncherProfiles {

  /**
   * Retrieves the clientToken from the launcher_profiles.json which is used for authentication with mojang.
   *
   * @return The non-null clientToken
   */
  String getClientToken();

  /**
   * Retrieves the version out of the launcher_profiles.json if a serializer is present for it.
   *
   * @return The version or {@link LauncherProfileResolver#getHighestSerializerVersion()} if no serializer is registered
   * with the version from the file
   */
  int getPreferredVersion();

  /**
   * Retrieves all profiles from the launcher_profiles.json that have been collected by the {@link
   * net.labyfy.component.session.launcher.serializer.LauncherProfileSerializer}.
   *
   * @return The non-null collection of all non-null profiles from the file
   */
  Collection<LauncherProfile> getProfiles();

  /**
   * Factory for the {@link LauncherProfiles}.
   */
  @AssistedFactory(LauncherProfiles.class)
  interface Factory {

    /**
     * Creates new {@link LauncherProfiles} with the given parameters.
     *
     * @param clientToken      The non-null clientToken from the launcher_profiles.json
     * @param preferredVersion The version which should be used to serialize the new object
     * @param profiles         The non-null collection of all non-null profiles from the launcher_profiles.json
     * @return The new non-null {@link LauncherProfiles}
     */
    LauncherProfiles create(@Assisted("clientToken") String clientToken, @Assisted("preferredVersion") int preferredVersion,
                            @Assisted("profiles") Collection<LauncherProfile> profiles);

  }

}
