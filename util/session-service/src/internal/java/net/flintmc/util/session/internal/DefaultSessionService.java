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

package net.flintmc.util.session.internal;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.gameprofile.GameProfile.Builder;
import net.flintmc.util.mojang.MojangUUIDMapper;
import net.flintmc.util.session.AuthenticationResult;
import net.flintmc.util.session.AuthenticationResult.Type;
import net.flintmc.util.session.RefreshTokenResult;
import net.flintmc.util.session.RefreshTokenResult.Factory;
import net.flintmc.util.session.SessionService;
import net.flintmc.util.session.event.SessionAccountLogInEvent;
import net.flintmc.util.session.event.SessionTokenRefreshEvent;
import org.apache.logging.log4j.Logger;

@Singleton
@Implement(SessionService.class)
public class DefaultSessionService implements SessionService {

  private static final String REFRESH_TOKEN_URL = "https://authserver.mojang.com/refresh";
  private static final String VALIDATE_TOKEN_URL = "https://authserver.mojang.com/validate";
  private static final String AUTHENTICATE_URL = "https://authserver.mojang.com/authenticate";

  private final Logger logger;

  private final RefreshTokenResult.Factory refreshTokenResultFactory;
  private final AuthenticationResult.Factory authResultFactory;
  private final Provider<GameProfile.Builder> gameProfileBuilderFactory;
  private final EventBus eventBus;
  private final SessionAccountLogInEvent.Factory logInEventFactory;
  private final SessionTokenRefreshEvent.Factory tokenRefreshEventFactory;
  private final MinecraftSessionUpdater sessionUpdater;

  private GameProfile selectedProfile;
  private String clientToken;
  private String accessToken;

  @Inject
  private DefaultSessionService(
      Logger logger,
      Factory refreshTokenResultFactory,
      Provider<Builder> gameProfileBuilderFactory,
      SessionAccountLogInEvent.Factory logInEventFactory,
      SessionTokenRefreshEvent.Factory tokenRefreshEventFactory,
      AuthenticationResult.Factory authResultFactory,
      EventBus eventBus,
      MinecraftSessionUpdater sessionUpdater) {
    this.logger = logger;
    this.refreshTokenResultFactory = refreshTokenResultFactory;
    this.gameProfileBuilderFactory = gameProfileBuilderFactory;
    this.logInEventFactory = logInEventFactory;
    this.tokenRefreshEventFactory = tokenRefreshEventFactory;
    this.authResultFactory = authResultFactory;
    this.eventBus = eventBus;
    this.sessionUpdater = sessionUpdater;
  }

  private void refreshSession() {
    if (this.sessionUpdater != null) {
      this.sessionUpdater.update(this);
    }
  }

  protected void updateAuthenticationContent(UUID uniqueId, String name, String newAccessToken) {
    this.accessToken = newAccessToken;
    this.selectedProfile = this.gameProfileBuilderFactory.get()
        .setUniqueId(uniqueId)
        .setName(name)
        .build();
  }

  @Override
  public String getClientToken() {
    if (this.clientToken == null) {
      // no custom client token set, generate a new one
      this.clientToken = UUID.randomUUID().toString().replace("-", "");
    }

    return this.clientToken;
  }

  @Override
  public void setClientToken(String clientToken) {
    this.clientToken = clientToken;
    // remove the authentication data because it gets invalid with a new clientToken
    this.logOut();
    // refresh the Session in the Minecraft client
    this.refreshSession();
  }

  @Override
  public UUID getUniqueId() {
    return this.selectedProfile != null ? this.selectedProfile.getUniqueId() : null;
  }

  @Override
  public String getUsername() {
    return this.selectedProfile != null ? this.selectedProfile.getName() : null;
  }

  @Override
  public GameProfile getProfile() {
    return this.selectedProfile;
  }

  @Override
  public String getAccessToken() {
    return this.accessToken;
  }

  @Override
  public boolean isAccessTokenValid() {
    String accessToken = this.getAccessToken();
    if (accessToken == null) {
      return false;
    }

    try {
      return this.validateAccessToken(accessToken);
    } catch (IOException e) {
      this.logger.error("An error occurred while validating access token", e);
      return false;
    }
  }

  @Override
  public boolean isLoggedIn() {
    return this.accessToken != null && !this.accessToken.isEmpty();
  }

  @Override
  public RefreshTokenResult refreshToken() {
    String accessToken = this.getAccessToken();
    if (accessToken == null) {
      return this.refreshTokenResultFactory.createUnknown(
          RefreshTokenResult.ResultType.NOT_LOGGED_IN);
    }

    try {
      JsonObject result = this.requestNewToken(accessToken);

      if (result == null) {
        return this.refreshTokenResultFactory.createUnknown(RefreshTokenResult.ResultType.OTHER);
      }

      if (result.has("accessToken")) {
        String newToken = result.get("accessToken").getAsString();

        // fire the event if enabled
        if (this.tokenRefreshEventFactory != null) {
          this.eventBus.fireEvent(
              this.tokenRefreshEventFactory.create(accessToken, newToken), Subscribe.Phase.POST);
        }

        this.accessToken = newToken;
        this.refreshSession();
        return this.refreshTokenResultFactory.createUnknown(RefreshTokenResult.ResultType.SUCCESS);
      }

      if (result.has("errorMessage")) {
        return this.refreshTokenResultFactory.create(
            RefreshTokenResult.ResultType.OTHER, result.get("errorMessage").getAsString());
      }

      return this.refreshTokenResultFactory.createUnknown(RefreshTokenResult.ResultType.OTHER);
    } catch (IOException | JsonParseException e) {
      return this.refreshTokenResultFactory.create(
          RefreshTokenResult.ResultType.OTHER, e.getMessage());
    }
  }

  private byte[] generateAuthenticateBody(String email, String password) {
    JsonObject object = new JsonObject();

    JsonObject agent = new JsonObject();
    object.add("agent", agent);
    agent.addProperty("name", "Minecraft");
    agent.addProperty("version", 1);

    object.addProperty("username", email);
    object.addProperty("password", password);
    object.addProperty("clientToken", this.getClientToken());

    return object.toString().getBytes(StandardCharsets.UTF_8);
  }

  private JsonObject authenticate(String email, String password) throws IOException {
    byte[] body = this.generateAuthenticateBody(email, password);

    HttpURLConnection connection = this.openConnection(AUTHENTICATE_URL, body);
    connection.setDoInput(true);

    try (OutputStream outputStream = connection.getOutputStream()) {
      outputStream.write(body);
    }

    boolean ok = connection.getResponseCode() >= 200 && connection.getResponseCode() < 300;

    try (InputStream inputStream = ok ? connection.getInputStream() : connection.getErrorStream();
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      return JsonParser.parseReader(reader).getAsJsonObject();
    }
  }

  private byte[] generateAccessTokenBody(String accessToken) {
    JsonObject body = new JsonObject();

    body.addProperty("clientToken", this.clientToken);
    body.addProperty("accessToken", accessToken);

    return body.toString().getBytes(StandardCharsets.UTF_8);
  }

  private boolean validateAccessToken(String accessToken) throws IOException {
    byte[] body = this.generateAccessTokenBody(accessToken);

    HttpURLConnection connection = this.openConnection(VALIDATE_TOKEN_URL, body);

    try (OutputStream outputStream = connection.getOutputStream()) {
      outputStream.write(body);
    }

    return connection.getResponseCode() == 204;
  }

  private HttpURLConnection openConnection(String url, byte[] body) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    connection.setRequestProperty("Content-Length", String.valueOf(body.length));
    connection.setDoOutput(true);

    return connection;
  }

  private JsonObject requestNewToken(String accessToken) throws IOException {
    byte[] body = this.generateAccessTokenBody(accessToken);

    HttpURLConnection connection = (HttpURLConnection) new URL(REFRESH_TOKEN_URL).openConnection();
    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    connection.setRequestProperty("Content-Length", String.valueOf(body.length));
    connection.setDoOutput(true);
    connection.setDoInput(true);

    try (OutputStream outputStream = connection.getOutputStream()) {
      outputStream.write(body);
    }

    if (connection.getResponseCode() < 200 || connection.getResponseCode() > 299) {
      return null;
    }

    try (InputStream inputStream = connection.getInputStream();
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      return JsonParser.parseReader(reader).getAsJsonObject();
    }
  }

  @Override
  public AuthenticationResult logIn(String email, String password) {
    if (email.isEmpty() || password.isEmpty()) {
      return this.authResultFactory.createFailed(Type.INVALID_CREDENTIALS);
    }

    GameProfile currentProfile = this.getProfile();

    this.logOut();

    try {
      JsonObject object = this.authenticate(email, password);

      if (object.has("cause") || object.has("error")) {
        String cause = object.has("cause") ? object.get("cause").getAsString() : null;
        String error = object.has("error") ? object.get("error").getAsString() : null;
        if ("UserMigratedException".equals(cause)) {
          return this.authResultFactory.createFailed(Type.USER_MIGRATED);
        }
        if ("ForbiddenOperationException".equals(error)) {
          return this.authResultFactory.createFailed(Type.INVALID_CREDENTIALS);
        }
      }

      if (object.has("selectedProfile")) {
        JsonObject profile = object.getAsJsonObject("selectedProfile");
        this.selectedProfile = this.gameProfileBuilderFactory.get()
            .setUniqueId(MojangUUIDMapper.fromMojangString(profile.get("id").getAsString()))
            .setName(profile.get("name").getAsString())
            .build();
      }

      if (object.has("accessToken")) {
        this.accessToken = object.get("accessToken").getAsString();
      }

      this.fireLoginEvent(currentProfile);

      return this.authResultFactory.createSuccess(this.getProfile());
    } catch (IOException e) {
      this.logger.error("An error occurred while logging into an account", e);
      return this.authResultFactory.createFailed(Type.AUTH_SERVER_OFFLINE);
    }
  }

  @Override
  public AuthenticationResult logIn(String accessToken) {
    GameProfile currentProfile = this.getProfile();

    try {
      JsonObject object = this.requestNewToken(accessToken);
      if (object == null || !object.has("accessToken")) {
        return this.authResultFactory.createFailed(Type.INVALID_CREDENTIALS);
      }
      JsonObject profile = object.get("selectedProfile").getAsJsonObject();

      this.logOut();

      this.updateAuthenticationContent(
          MojangUUIDMapper.fromMojangString(profile.get("id").getAsString()),
          profile.get("name").getAsString(),
          object.get("accessToken").getAsString());

      this.fireLoginEvent(currentProfile);

      return this.authResultFactory.createSuccess(this.getProfile());
    } catch (IOException e) {
      this.logger.error("Failed to log in using access token", e);
      return this.authResultFactory.createFailed(Type.AUTH_SERVER_OFFLINE);
    }
  }

  private void fireLoginEvent(GameProfile previousProfile) {
    GameProfile newProfile = this.getProfile();

    if (this.logInEventFactory != null) {
      SessionAccountLogInEvent event =
          previousProfile != null
              ? this.logInEventFactory.create(previousProfile, newProfile)
              : this.logInEventFactory.create(newProfile);
      this.eventBus.fireEvent(event, Subscribe.Phase.POST);
    }

    this.refreshSession();
  }

  @Override
  public void logOut() {
    if (this.isLoggedIn()) {
      this.selectedProfile = null;
      this.accessToken = null;
    }
  }

  @Override
  public boolean isMain() {
    return this.tokenRefreshEventFactory != null
        && this.logInEventFactory != null
        && this.sessionUpdater != null;
  }

  @Override
  public SessionService newSessionService() {
    return new DefaultSessionService(
        this.logger,
        this.refreshTokenResultFactory,
        this.gameProfileBuilderFactory,
        this.logInEventFactory,
        this.tokenRefreshEventFactory,
        this.authResultFactory,
        this.eventBus,
        this.sessionUpdater);
  }
}
