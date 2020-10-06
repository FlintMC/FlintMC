package net.labyfy.internal.component.session;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class DefaultSessionService implements SessionService {

  private static final String REFRESH_TOKEN_URL = "https://authserver.mojang.com/refresh";
  private static final String VALIDATE_TOKEN_URL = "https://authserver.mojang.com/validate";

  private final ExecutorService executorService;
  private final Logger logger;

  private final RefreshTokenResult.Factory refreshTokenResultFactory;
  private final AuthenticationResult.Factory authResultFactory;
  private final UserAuthentication authentication;
  private final String clientToken;
  private final GameProfileSerializer<com.mojang.authlib.GameProfile> profileSerializer;

  private final SessionAccountLogInEvent.Factory logInEventFactory;
  private final SessionTokenRefreshEvent.Factory tokenRefreshEventFactory;

  public DefaultSessionService(Logger logger, RefreshTokenResult.Factory refreshTokenResultFactory,
                               GameProfileSerializer profileSerializer,
                               SessionAccountLogInEvent.Factory logInEventFactory,
                               SessionTokenRefreshEvent.Factory tokenRefreshEventFactory,
                               AuthenticationResult.Factory authResultFactory,
                               Proxy minecraftProxy) {
    this.logger = logger;
    this.refreshTokenResultFactory = refreshTokenResultFactory;
    this.profileSerializer = profileSerializer;
    this.logInEventFactory = logInEventFactory;
    this.tokenRefreshEventFactory = tokenRefreshEventFactory;
    this.authResultFactory = authResultFactory;
    this.executorService = Executors.newFixedThreadPool(1);

    this.clientToken = UUID.randomUUID().toString();
    AuthenticationService authenticationService = new YggdrasilAuthenticationService(minecraftProxy, this.clientToken);
    this.authentication = authenticationService.createUserAuthentication(Agent.MINECRAFT);
  }

  protected abstract void refreshSession();

  @Override
  public UUID getUniqueId() {
    com.mojang.authlib.GameProfile profile = this.authentication.getSelectedProfile();
    return profile != null ? profile.getId() : null;
  }

  @Override
  public String getUsername() {
    com.mojang.authlib.GameProfile profile = this.authentication.getSelectedProfile();
    return profile != null ? profile.getName() : null;
  }

  @Override
  public GameProfile getProfile() {
    com.mojang.authlib.GameProfile profile = this.authentication.getSelectedProfile();
    return profile != null ? this.profileSerializer.deserialize(profile) : null;
  }

  @Override
  public String getAccessToken() {
    return this.authentication.getAuthenticatedToken();
  }

  @Override
  public CompletableFuture<Boolean> isAccessTokenValid() {
    String accessToken = this.getAccessToken();
    if (accessToken == null) {
      return CompletableFuture.completedFuture(false);
    }

    CompletableFuture<Boolean> future = new CompletableFuture<>();

    this.executorService.execute(() -> {
      try {
        future.complete(this.validateAccessToken(accessToken));
      } catch (IOException e) {
        this.logger.error("An error occurred while validating access token", e);
        future.complete(false);
      }
    });

    return future;
  }

  @Override
  public boolean isLoggedIn() {
    return this.authentication.isLoggedIn();
  }

  @Override
  public CompletableFuture<RefreshTokenResult> refreshToken() {
    String accessToken = this.getAccessToken();
    if (accessToken == null) {
      return CompletableFuture.completedFuture(this.refreshTokenResultFactory.createUnknown(RefreshTokenResult.ResultType.NOT_LOGGED_IN));
    }
    if (!(this.authentication instanceof RefreshableUserAuthentication)) {
      // this can only happen if shadow has failed which basically never happens
      return CompletableFuture.completedFuture(this.refreshTokenResultFactory.create(RefreshTokenResult.ResultType.OTHER, "Not supported"));
    }

    RefreshableUserAuthentication refreshable = (RefreshableUserAuthentication) this.authentication;
    CompletableFuture<RefreshTokenResult> future = new CompletableFuture<>();

    this.executorService.execute(() -> {
      try {
        JsonObject result = this.requestNewToken(accessToken);

        if (result.has("accessToken")) {
          String newToken = result.get("accessToken").getAsString();

          // TODO fire this event
          this.tokenRefreshEventFactory.create(accessToken, newToken);

          refreshable.setAccessToken(newToken);
          this.refreshSession();
          future.complete(this.refreshTokenResultFactory.createUnknown(RefreshTokenResult.ResultType.SUCCESS));
          return;
        }

        if (result.has("errorMessage")) {
          future.complete(this.refreshTokenResultFactory.create(RefreshTokenResult.ResultType.OTHER, result.get("errorMessage").getAsString()));
          return;
        }

        future.complete(this.refreshTokenResultFactory.createUnknown(RefreshTokenResult.ResultType.OTHER));
      } catch (IOException | JsonParseException e) {
        future.complete(this.refreshTokenResultFactory.create(RefreshTokenResult.ResultType.OTHER, e.getMessage()));
      }
    });

    return future;
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
  public CompletableFuture<AuthenticationResult> logIn(String email, String password) {
    GameProfile currentProfile = this.getProfile();

    if (this.authentication.isLoggedIn()) {
      this.authentication.logOut();
    }

    CompletableFuture<AuthenticationResult> future = new CompletableFuture<>();

    this.executorService.execute(() -> {
      this.authentication.setUsername(email);
      this.authentication.setPassword(password);

      try {
        this.authentication.logIn();

        this.refreshSession();

        GameProfile newProfile = this.getProfile();

        // TODO fire this event
        SessionAccountLogInEvent event = currentProfile != null ?
            this.logInEventFactory.create(currentProfile, newProfile) :
            this.logInEventFactory.create(newProfile);

        future.complete(this.authResultFactory.createSuccess(newProfile));
      } catch (AuthenticationUnavailableException e) {
        future.complete(this.authResultFactory.createFailed(Type.AUTH_SERVER_OFFLINE));
      } catch (InvalidCredentialsException e) {
        future.complete(this.authResultFactory.createFailed(Type.INVALID_CREDENTIALS));
      } catch (AuthenticationException e) {
        this.logger.error("An error occurred while logging into an account", e);
        future.complete(this.authResultFactory.createFailed(Type.UNKNOWN_ERROR));
      }
    });

    return future;
  }
}
