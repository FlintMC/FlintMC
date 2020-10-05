package net.labyfy.internal.component.session.refresh;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.session.refresh.RefreshTokenResult;

@Implement(RefreshTokenResult.class)
public class DefaultRefreshTokenResult implements RefreshTokenResult {

  private final boolean success;
  private final String errorMessage;

  @AssistedInject
  public DefaultRefreshTokenResult(@Assisted("success") boolean success) {
    this(success, null);
  }

  @AssistedInject
  public DefaultRefreshTokenResult(@Assisted("success") boolean success, @Assisted("errorMessage") String errorMessage) {
    this.success = success;
    this.errorMessage = errorMessage;
  }

  @Override
  public boolean wasSuccess() {
    return this.success;
  }

  @Override
  public String getErrorMessage() {
    return this.errorMessage;
  }
}
