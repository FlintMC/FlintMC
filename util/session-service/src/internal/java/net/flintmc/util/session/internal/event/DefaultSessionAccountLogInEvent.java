package net.flintmc.util.session.internal.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.util.session.event.SessionAccountLogInEvent;

@Implement(SessionAccountLogInEvent.class)
public class DefaultSessionAccountLogInEvent implements SessionAccountLogInEvent {

  private final GameProfile previousProfile;
  private final GameProfile newProfile;

  @AssistedInject
  private DefaultSessionAccountLogInEvent(
      @Assisted("previousProfile") GameProfile previousProfile,
      @Assisted("newProfile") GameProfile newProfile) {
    this.previousProfile = previousProfile;
    this.newProfile = newProfile;
  }

  @AssistedInject
  public DefaultSessionAccountLogInEvent(@Assisted("newProfile") GameProfile newProfile) {
    this(null, newProfile);
  }

  @Override
  public GameProfile getPreviousProfile() {
    return this.previousProfile;
  }

  @Override
  public GameProfile getNewProfile() {
    return this.newProfile;
  }
}
