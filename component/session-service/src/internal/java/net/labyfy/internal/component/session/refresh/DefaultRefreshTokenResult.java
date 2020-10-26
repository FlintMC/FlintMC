package net.labyfy.internal.component.session.refresh;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.session.RefreshTokenResult;

@Implement(RefreshTokenResult.class)
public class DefaultRefreshTokenResult implements RefreshTokenResult {

  private final ResultType type;
  private final String errorMessage;

  @AssistedInject
  private DefaultRefreshTokenResult(@Assisted("type") ResultType type) {
    this(type, null);
  }

  @AssistedInject
  private DefaultRefreshTokenResult(@Assisted("type") ResultType type, @Assisted("errorMessage") String errorMessage) {
    this.type = type;
    this.errorMessage = errorMessage;
  }

  @Override
  public ResultType getType() {
    return this.type;
  }

  @Override
  public String getErrorMessage() {
    return this.errorMessage;
  }
}
