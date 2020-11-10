package net.flintmc.util.session.internal.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.session.event.SessionTokenRefreshEvent;

@Implement(SessionTokenRefreshEvent.class)
public class DefaultSessionTokenRefreshEvent implements SessionTokenRefreshEvent {

  private final String previousToken;
  private final String newToken;

  @AssistedInject
  private DefaultSessionTokenRefreshEvent(
      @Assisted("previousToken") String previousToken, @Assisted("newToken") String newToken) {
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
