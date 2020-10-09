package net.labyfy.internal.component.session;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.session.AuthenticationResult;
import net.labyfy.component.session.AuthenticationResult.Type;
import net.labyfy.component.session.RefreshTokenResult;
import net.labyfy.component.session.SessionService;
import net.labyfy.component.session.event.SessionAccountLogInEvent;
import net.labyfy.component.session.event.SessionTokenRefreshEvent;
import net.labyfy.internal.component.session.refresh.RefreshableUserAuthentication;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public abstract class DefaultSessionService implements SessionService {

  private static final String REFRESH_TOKEN_URL = "https://authserver.mojang.com/refresh";
  private static final String VALIDATE_TOKEN_URL = "https://authserver.mojang.com/validate";

  private final Logger logger;

  private final Proxy minecraftProxy;

  private final RefreshTokenResult.Factory refreshTokenResultFactory;
  private final AuthenticationResult.Factory authResultFactory;

  private UserAuthentication authentication;
  private String clientToken;

  private final GameProfileSerializer<com.mojang.authlib.GameProfile> profileSerializer;

  private final EventBus eventBus;
  private final SessionAccountLogInEvent.Factory logInEventFactory;
  private final SessionTokenRefreshEvent.Factory tokenRefreshEventFactory;

  protected DefaultSessionService(Logger logger, RefreshTokenResult.Factory refreshTokenResultFactory,
                                  GameProfileSerializer profileSerializer,
                                  SessionAccountLogInEvent.Factory logInEventFactory,
                                  SessionTokenRefreshEvent.Factory tokenRefreshEventFactory,
                                  AuthenticationResult.Factory authResultFactory,
                                  EventBus eventBus,
                                  Proxy minecraftProxy) {
    this.logger = logger;
    this.refreshTokenResultFactory = refreshTokenResultFactory;
    this.minecraftProxy = minecraftProxy;
    this.profileSerializer = profileSerializer;
    this.logInEventFactory = logInEventFactory;
    this.tokenRefreshEventFactory = tokenRefreshEventFactory;
    this.authResultFactory = authResultFactory;
    this.eventBus = eventBus;
  }

  protected abstract void refreshSession();

  private UserAuthentication ensureAuthenticationAvailable() {
    if (this.authentication == null) {
      if (this.clientToken == null) {
        this.clientToken = UUID.randomUUID().toString().replace("-", "");
      }

      this.authentication = new YggdrasilAuthenticationService(this.minecraftProxy, this.clientToken).createUserAuthentication(Agent.MINECRAFT);
    }

    return this.authentication;
  }

  @Override
  public void setClientToken(String clientToken) {
    this.clientToken = clientToken;
    this.authentication = null;
    this.refreshSession();
  }

  @Override
  public UUID getUniqueId() {
    if (this.authentication == null) {
      return null;
    }
    com.mojang.authlib.GameProfile profile = this.authentication.getSelectedProfile();
    return profile != null ? profile.getId() : null;
  }

  @Override
  public String getUsername() {
    if (this.authentication == null) {
      return null;
    }
    com.mojang.authlib.GameProfile profile = this.authentication.getSelectedProfile();
    return profile != null ? profile.getName() : null;
  }

  @Override
  public GameProfile getProfile() {
    if (this.authentication == null) {
      return null;
    }
    com.mojang.authlib.GameProfile profile = this.authentication.getSelectedProfile();
    return profile != null ? this.profileSerializer.deserialize(profile) : null;
  }

  @Override
  public String getAccessToken() {
    return this.authentication != null ? this.authentication.getAuthenticatedToken() : null;
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
    return this.authentication != null && this.authentication.isLoggedIn();
  }

  @Override
  public RefreshTokenResult refreshToken() {
    String accessToken = this.getAccessToken();
    if (accessToken == null) {
      return this.refreshTokenResultFactory.createUnknown(RefreshTokenResult.ResultType.NOT_LOGGED_IN);
    }
    UserAuthentication authentication = this.ensureAuthenticationAvailable();

    if (!(authentication instanceof RefreshableUserAuthentication)) {
      // this can only happen if shadow has failed which basically never happens
      return this.refreshTokenResultFactory.create(RefreshTokenResult.ResultType.OTHER, "Not supported");
    }

    try {
      RefreshableUserAuthentication refreshable = (RefreshableUserAuthentication) authentication;

      JsonObject result = this.requestNewToken(accessToken);

      if (result.has("accessToken")) {
        String newToken = result.get("accessToken").getAsString();

        this.eventBus.fireEvent(this.tokenRefreshEventFactory.create(accessToken, newToken), Subscribe.Phase.POST);

        refreshable.setAccessToken(newToken);
        this.refreshSession();
        return this.refreshTokenResultFactory.createUnknown(RefreshTokenResult.ResultType.SUCCESS);
      }

      if (result.has("errorMessage")) {
        return this.refreshTokenResultFactory.create(RefreshTokenResult.ResultType.OTHER, result.get("errorMessage").getAsString());
      }

      return this.refreshTokenResultFactory.createUnknown(RefreshTokenResult.ResultType.OTHER);
    } catch (IOException | JsonParseException e) {
      return this.refreshTokenResultFactory.create(RefreshTokenResult.ResultType.OTHER, e.getMessage());
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

    HttpURLConnection connection = (HttpURLConnection) new URL(VALIDATE_TOKEN_URL).openConnection();
    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    connection.setRequestProperty("Content-Length", String.valueOf(body.length));
    connection.setDoOutput(true);

    try (OutputStream outputStream = connection.getOutputStream()) {
      outputStream.write(body);
    }

    return connection.getResponseCode() == 204;
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

    try (InputStream inputStream = connection.getInputStream();
         Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      return JsonParser.parseReader(reader).getAsJsonObject();
    }
  }

  @Override
  public AuthenticationResult logIn(String email, String password) {
    GameProfile currentProfile = this.getProfile();
    UserAuthentication authentication = this.ensureAuthenticationAvailable();

    if (authentication.isLoggedIn()) {
      authentication.logOut();
    }

    authentication.setUsername(email);
    authentication.setPassword(password);

    try {
      authentication.logIn();

      this.refreshSession();

      GameProfile newProfile = this.getProfile();

      SessionAccountLogInEvent event = currentProfile != null ?
          this.logInEventFactory.create(currentProfile, newProfile) :
          this.logInEventFactory.create(newProfile);
      this.eventBus.fireEvent(event, Subscribe.Phase.POST);

      return this.authResultFactory.createSuccess(newProfile);
    } catch (AuthenticationUnavailableException e) {
      return this.authResultFactory.createFailed(Type.AUTH_SERVER_OFFLINE);
    } catch (InvalidCredentialsException e) {
      return this.authResultFactory.createFailed(Type.INVALID_CREDENTIALS);
    } catch (AuthenticationException e) {
      this.logger.error("An error occurred while logging into an account", e);
      return this.authResultFactory.createFailed(Type.UNKNOWN_ERROR);
    }
  }

  @Override
  public void logOut() {
    if (this.authentication != null && this.authentication.isLoggedIn()) {
      this.authentication.logOut();
    }
  }
}
