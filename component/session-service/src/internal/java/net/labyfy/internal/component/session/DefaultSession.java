package net.labyfy.internal.component.session;

import com.google.gson.JsonObject;
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
import net.labyfy.component.session.Session;
import net.labyfy.component.session.refresh.NotLoggedInException;
import net.labyfy.component.session.refresh.RefreshTokenResult;
import net.labyfy.internal.component.session.refresh.RefreshableUserAuthentication;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class DefaultSession implements Session {

  private static final String REFRESH_TOKEN_URL = "https://authserver.mojang.com/refresh";

  private final ExecutorService executorService;

  private final RefreshTokenResult.Factory refreshTokenResultFactory;
  private final UserAuthentication authentication;
  private final String clientToken;
  private final GameProfileSerializer<com.mojang.authlib.GameProfile> profileSerializer;
  private String email;

  public DefaultSession(RefreshTokenResult.Factory refreshTokenResultFactory, GameProfileSerializer profileSerializer,
                        Proxy minecraftProxy) {
    this.refreshTokenResultFactory = refreshTokenResultFactory;
    this.profileSerializer = profileSerializer;
    this.executorService = Executors.newFixedThreadPool(1);

    this.clientToken = UUID.randomUUID().toString();
    AuthenticationService authenticationService = new YggdrasilAuthenticationService(minecraftProxy, this.clientToken);
    this.authentication = authenticationService.createUserAuthentication(Agent.MINECRAFT);
  }

  protected abstract void refreshSession();

  @Override
  public String getEmail() {
    return this.email;
  }

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
  public boolean isLoggedIn() {
    return this.authentication.isLoggedIn();
  }

  @Override
  public CompletableFuture<RefreshTokenResult> refreshToken() {
    if (this.email == null) {
      throw NotLoggedInException.INSTANCE;
    }
    if (!(this.authentication instanceof RefreshableUserAuthentication)) {
      return CompletableFuture.completedFuture(this.refreshTokenResultFactory.create(false, "Not supported"));
    }

    RefreshableUserAuthentication refreshable = (RefreshableUserAuthentication) this.authentication;
    CompletableFuture<RefreshTokenResult> future = new CompletableFuture<>();

    this.executorService.execute(() -> {
      try {
        JsonObject result = this.requestNewToken();

        if (result.has("accessToken")) {
          // TODO token refresh event?
          refreshable.setAccessToken(result.get("accessToken").getAsString());
          this.refreshSession();
          future.complete(this.refreshTokenResultFactory.create(true));
          return;
        }

        if (result.has("errorMessage")) {
          future.complete(this.refreshTokenResultFactory.create(false, result.get("errorMessage").getAsString()));
          return;
        }

        future.complete(this.refreshTokenResultFactory.create(false));
      } catch (IOException e) {
        future.complete(this.refreshTokenResultFactory.create(false, e.getMessage()));
      }
    });

    return future;
  }

  private JsonObject requestNewToken() throws IOException {
    JsonObject body = new JsonObject();
    body.addProperty("clientToken", this.clientToken);
    body.addProperty("accessToken", this.getAccessToken());

    byte[] bytes = body.toString().getBytes(StandardCharsets.UTF_8);

    HttpURLConnection connection = (HttpURLConnection) new URL(REFRESH_TOKEN_URL).openConnection();
    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
    connection.setDoOutput(true);
    connection.setDoInput(true);

    try (OutputStream outputStream = connection.getOutputStream()) {
      outputStream.write(bytes);
    }

    try (InputStream inputStream = connection.getInputStream();
         Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      return JsonParser.parseReader(reader).getAsJsonObject();
    }
  }

  @Override
  public CompletableFuture<AuthenticationResult> logIn(String email, String password) {
    if (this.authentication.isLoggedIn()) {
      if (email.equals(this.email)) {
        return CompletableFuture.completedFuture(AuthenticationResult.ALREADY_LOGGED_IN);
      }

      this.authentication.logOut();
    }

    CompletableFuture<AuthenticationResult> future = new CompletableFuture<>();

    this.executorService.execute(() -> {
      this.authentication.setUsername(email);
      this.authentication.setPassword(password);

      try {
        this.authentication.logIn();

        this.email = email;
        this.refreshSession();

        future.complete(AuthenticationResult.SUCCESS);
      } catch (AuthenticationUnavailableException e) {
        future.complete(AuthenticationResult.AUTH_SERVER_OFFLINE);
      } catch (InvalidCredentialsException e) {
        future.complete(AuthenticationResult.INVALID_CREDENTIALS);
      } catch (AuthenticationException e) {
        future.complete(AuthenticationResult.UNKNOWN_ERROR);
      }
    });

    return future;
  }
}
