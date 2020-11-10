package net.flintmc.util.session;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.player.gameprofile.GameProfile;

/** The result of the {@link SessionService#logIn(String, String)} method. */
public interface AuthenticationResult {

  /**
   * Retrieves the type of this result.
   *
   * @return The non-null type of this result
   */
  Type getType();

  /**
   * Retrieves the new profile of the new account.
   *
   * @return The profile of the new account or {@code null} if an error occurred while logging in
   *     (and therefore the {@link #getType()} must not be {@link Type#SUCCESS})
   */
  GameProfile getProfile();

  /** Types of results for the {@link AuthenticationResult}. */
  enum Type {

    /** Specifies that the log in has successfully been completed. */
    SUCCESS,
    /** Specifies that the email and/or password provided to this method are invalid. */
    INVALID_CREDENTIALS,
    /**
     * If this result is returned, the mojang auth server is offline and therefore no login is
     * possible.
     */
    AUTH_SERVER_OFFLINE,
    /**
     * Any other error that might occur while logging in and is not specified by this result. Check
     * the log for more details!
     */
    UNKNOWN_ERROR
  }

  /** Factory for the {@link AuthenticationResult}. */
  @AssistedFactory(AuthenticationResult.class)
  interface Factory {

    /**
     * Creates a new {@link AuthenticationResult} with the given type and no profile, which means
     * that the result cannot be success.
     *
     * @param type The non-null type which must not be {@link Type#SUCCESS}
     * @return The new non-null result
     */
    AuthenticationResult createFailed(@Assisted("type") Type type);

    /**
     * Creates a new {@link AuthenticationResult} with the given profile and the type {@link
     * Type#SUCCESS}, which means that the profile cannot be {@code null}.
     *
     * @param profile The non-null profile of the new account
     * @return The new non-null result
     */
    AuthenticationResult createSuccess(@Assisted("profile") GameProfile profile);
  }
}
