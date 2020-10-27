package net.flintmc.util.session.event;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.util.session.SessionService;

/**
 * This event will be fired in the POST state whenever the {@link SessionService} logs into an account.
 *
 * @see SessionService#logIn(String, String)
 */
public interface SessionAccountLogInEvent {

  /**
   * Retrieves the game profile from before the log in (if the account was switched).
   *
   * @return The profile of the previous account or {@code null} if the client was not logged in when changing the
   * account
   */
  GameProfile getPreviousProfile();

  /**
   * Retrieves the profile of the new account which has been logged in.
   *
   * @return The non-null selected profile of the new account
   */
  GameProfile getNewProfile();

  /**
   * Factory for the {@link SessionAccountLogInEvent}.
   */
  @AssistedFactory(SessionAccountLogInEvent.class)
  interface Factory {

    /**
     * Creates a new {@link SessionAccountLogInEvent} with the given profiles.
     *
     * @param previousProfile The non-null profile before the login, if there was no profile before the login, use
     *                        {@link #create(GameProfile)} instead
     * @param newProfile      The non-null profile after the login
     * @return The new non-null {@link SessionAccountLogInEvent}
     */
    SessionAccountLogInEvent create(@Assisted("previousProfile") GameProfile previousProfile, @Assisted("newProfile") GameProfile newProfile);

    /**
     * Creates a new {@link SessionAccountLogInEvent} with the given profile and without a profile before the login.
     *
     * @param newProfile The non-null profile after the login
     * @return The new non-null {@link SessionAccountLogInEvent}
     */
    SessionAccountLogInEvent create(@Assisted("newProfile") GameProfile newProfile);

  }

}
