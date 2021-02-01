/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.util.session;

import java.util.UUID;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.mcapi.event.MinecraftInitializeEvent;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.util.session.event.SessionAccountLogInEvent;

/**
 * The SessionService can be used to change the minecraft account to be played with, get the current
 * logged in account and to refresh the access token which is used to login on servers.
 *
 * <p>It may not be used before the {@link MinecraftInitializeEvent} has been fired in the {@link
 * Subscribe.Phase#POST} phase.
 */
public interface SessionService {

  /**
   * Retrieves the uniqueId of the player of the account which is currently used by the client.
   *
   * @return The uniqueId or {@code null}, if the client is not logged into any account
   */
  UUID getUniqueId();

  /**
   * Retrieves the name of the player of the account which is currently used by the client.
   *
   * @return The name or {@code null}, if the client is not logged into any account
   */
  String getUsername();

  /**
   * Retrieves the profile of the player of the account which is currently used by the client.
   *
   * @return The profile or {@code null}, if the client is not logged into any account
   */
  GameProfile getProfile();

  /**
   * Retrieves the client token which is used to log in/log out/refresh the access token in this
   * {@link SessionService}.
   *
   * @return The non-null client token of this session service
   */
  String getClientToken();

  /**
   * Changes the client token for this session service. This method also "logs out" (the accessToken
   * will stay valid, but this SessionService won't use it anymore) from the account (if logged
   * in).
   *
   * <p>After calling this method, you should also {@link #logIn(String, String) log in} again.
   *
   * @param clientToken The new non-null client token to be used for the authentication
   */
  void setClientToken(String clientToken);

  /**
   * Retrieves the current access of the account which is currently used by the client. This token
   * will be used to log in on servers. If the token is invalid, it can be refreshed with {@link
   * #refreshToken()}. If the {@link SessionService} is logged in, but the token is no more valid,
   * this method will still return the token and not {@code null}.
   *
   * @return The access token or {@code null}, if the client is not logged into any account
   * @see #refreshToken()
   */
  String getAccessToken();

  /**
   * Retrieves whether the current access token is valid or needs to be refreshed by using {@link
   * #refreshToken()}.
   *
   * @return {@code true} if the access token is valid or {@code false} if either the access token
   * is invalid or the {@link SessionService} is not logged into an account6
   */
  boolean isAccessTokenValid();

  /**
   * Retrieves whether this SessionService is currently logged in, this doesn't check if the access
   * token is valid. To check for a valid access token, see {@link #isAccessTokenValid()}.
   *
   * @return {@code true} if the SessionService is logged in, {@code false} otherwise
   */
  boolean isLoggedIn();

  /**
   * Refreshes the access token of the current session asynchronously.
   *
   * @return The non-null result
   */
  RefreshTokenResult refreshToken();

  /**
   * Logs into an account with the given email and password asynchronously. If the username instead
   * of the email is provided, an {@link AuthenticationResult} with the type {@link
   * AuthenticationResult.Type#INVALID_CREDENTIALS} will be returned.
   *
   * @param email    The non-null email of the account
   * @param password The non-null password of the account
   * @return The non-null result of the log in
   */
  AuthenticationResult logIn(String email, String password);

  /**
   * Logs into an account with the given access token. For this to work the {@link
   * #setClientToken(String) clientToken} needs to be the same as it was when the access token has
   * been obtained.
   *
   * @param accessToken The non-null access token of the session
   * @return The non-null result of the log in
   */
  AuthenticationResult logIn(String accessToken);

  /**
   * Logs this SessionService out of the given account or does nothing if it is not logged in.
   */
  void logOut();

  /**
   * Retrieves whether this instance is the main {@link SessionService} out of the injector. If it
   * is not the main instance, this session service won't fire any events and won't have any effect
   * on the session of the Minecraft client. This method can't return {@code true} if this instance
   * has been created with {@link #newSessionService()}.
   *
   * @return {@code true} if this instance is the main {@link SessionService}, {@code false}
   * otherwise
   */
  boolean isMain();

  /**
   * Creates a new {@link SessionService} which is not a singleton instance and doesn't have the
   * current account of this one (if this one is logged in). The new session service won't fire any
   * events like the {@link SessionAccountLogInEvent} and it doesn't have any effects on the session
   * of the Minecraft client (logging in on the new service doesn't change the account of the
   * client), it can just be used for logging into an account and getting information about it.
   *
   * @return The new non-null session service
   */
  SessionService newSessionService();
}
