package net.labyfy.component.session;

import net.labyfy.component.player.gameprofile.GameProfile;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * The SessionService can be used to change the minecraft account to be played with, get the current logged in account
 * and to refresh the access token which is used to login on servers.
 */
public interface SessionService {

  /**
   * Retrieves the uniqueId of the player of the account which is currently used by the client.
   *
   * @return The uniqueId or {@code null} if the client is not logged into any account
   */
  UUID getUniqueId();

  /**
   * Retrieves the name of the player of the account which is currently used by the client.
   *
   * @return The name or {@code null} if the client is not logged into any account
   */
  String getUsername();

  /**
   * Retrieves the profile of the player of the account which is currently used by the client.
   *
   * @return The profile or {@code null} if the client is not logged into any account
   */
  GameProfile getProfile();

  /**
   * Retrieves the current access of the account which is currently used by the client. This token will be used to log
   * in on servers. If the token is invalid, it can be refreshed with {@link #refreshToken()}. If the {@link
   * SessionService} is logged in, but the token is no more valid, this method will still return the token and not
   * {@code null}.
   *
   * @return The access token or {@code null} if the client is not logged into any account
   * @see #refreshToken()
   */
  String getAccessToken();

  /**
   * Retrieves whether the current access token is valid or needs to be refreshed by using {@link #refreshToken()}.
   *
   * @return A non-null completable future which will be completed with {@code true} if the access token is valid or
   * {@code false} if either the access token is invalid or the {@link SessionService} is not logged into an account6
   */
  CompletableFuture<Boolean> isAccessTokenValid();

  /**
   * Retrieves whether this SessionService is currently logged in, this doesn't check if the access token is valid. To
   * check for a valid access token, see {@link #isAccessTokenValid()}.
   *
   * @return {@code true} if the SessionService is logged in, {@code false} otherwise
   */
  boolean isLoggedIn();

  /**
   * Refreshes the access token of the current session asynchronously.
   *
   * @return A non-null completable future which will be completed with the result
   */
  CompletableFuture<RefreshTokenResult> refreshToken();

  /**
   * Logs into an account with the given email and password asynchronously. If the username instead of the email is
   * provided, an {@link AuthenticationResult} with the type {@link AuthenticationResult.Type#INVALID_CREDENTIALS} will
   * be returned.
   *
   * @param email    The non-null email of the account
   * @param password The non-null password of the account
   * @return A non-null completable future which will be completed with the result
   */
  CompletableFuture<AuthenticationResult> logIn(String email, String password);

}
