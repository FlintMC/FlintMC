package net.flintmc.util.session.event;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.util.session.SessionService;

/**
 * This event will be fired in the POST state whenever the {@link SessionService} has refreshed its
 * token.
 *
 * @see SessionService#refreshToken()
 */
public interface SessionTokenRefreshEvent {

  /**
   * Retrieves the access token that has been used before the token has been refreshed.
   *
   * @return The non-null access token before it has been refreshed
   */
  String getPreviousAccessToken();

  /**
   * Retrieves the new access token from the refresh.
   *
   * @return The non-null access token after it has been refreshed
   */
  String getNewAccessToken();

  /** Factory for the {@link SessionTokenRefreshEvent}. */
  @AssistedFactory(SessionTokenRefreshEvent.class)
  interface Factory {

    /**
     * Creates a new {@link SessionTokenRefreshEvent} with the given tokens.
     *
     * @param previousToken The non-null token from before it has been refreshed (this is always
     *     given because the token cannot be refreshed without having a token)
     * @param newToken The non-null token after it has been refreshed
     * @return The new non-null {@link SessionTokenRefreshEvent}
     */
    SessionTokenRefreshEvent create(
        @Assisted("previousToken") String previousToken, @Assisted("newToken") String newToken);
  }
}
