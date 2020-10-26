package net.labyfy.internal.component.session.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.session.event.SessionAccountLogInEvent;

@Implement(SessionAccountLogInEvent.class)
public class DefaultSessionAccountLogInEvent implements SessionAccountLogInEvent {

  private final GameProfile previousProfile;
  private final GameProfile newProfile;

  @AssistedInject
  private DefaultSessionAccountLogInEvent(@Assisted("previousProfile") GameProfile previousProfile, @Assisted("newProfile") GameProfile newProfile) {
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
