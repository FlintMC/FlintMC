package net.flintmc.util.session.internal;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.util.session.AuthenticationResult;

@Implement(AuthenticationResult.class)
public class DefaultAuthenticationResult implements AuthenticationResult {

  private final Type type;
  private final GameProfile profile;

  @AssistedInject
  private DefaultAuthenticationResult(@Assisted("type") Type type) { // failed
    this(type, null);
  }

  @AssistedInject
  private DefaultAuthenticationResult(@Assisted("profile") GameProfile profile) { // success
    this(Type.SUCCESS, profile);
  }

  private DefaultAuthenticationResult(Type type, GameProfile profile) {
    this.type = type;
    this.profile = profile;
  }

  @Override
  public Type getType() {
    return this.type;
  }

  @Override
  public GameProfile getProfile() {
    return this.profile;
  }
}
