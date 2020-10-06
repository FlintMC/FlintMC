package net.labyfy.internal.component.session.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.session.event.SessionTokenRefreshEvent;

@Implement(SessionTokenRefreshEvent.class)
public class DefaultSessionTokenRefreshEvent implements SessionTokenRefreshEvent {

  private final String previousToken;
  private final String newToken;

  @AssistedInject
  public DefaultSessionTokenRefreshEvent(@Assisted("previousToken") String previousToken, @Assisted("newToken") String newToken) {
    this.previousToken = previousToken;
    this.newToken = newToken;
  }

  @Override
  public String getPreviousAccessToken() {
    return this.previousToken;
  }

  @Override
  public String getNewAccessToken() {
    return this.newToken;
  }
}
