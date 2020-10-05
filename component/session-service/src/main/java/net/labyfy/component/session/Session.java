package net.labyfy.component.session;

import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.session.refresh.RefreshTokenResult;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Session {

  String getEmail();

  UUID getUniqueId();

  String getUsername();

  GameProfile getProfile();

  String getAccessToken();

  boolean isLoggedIn();

  CompletableFuture<RefreshTokenResult> refreshToken();

  CompletableFuture<AuthenticationResult> logIn(String email, String password);

}
