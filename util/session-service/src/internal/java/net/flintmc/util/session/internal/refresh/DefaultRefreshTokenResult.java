package net.flintmc.util.session.internal.refresh;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.session.RefreshTokenResult;

@Implement(RefreshTokenResult.class)
public class DefaultRefreshTokenResult implements RefreshTokenResult {

  private final ResultType type;
  private final String errorMessage;

  @AssistedInject
  private DefaultRefreshTokenResult(@Assisted("type") ResultType type) {
    this(type, null);
  }

  @AssistedInject
  private DefaultRefreshTokenResult(
      @Assisted("type") ResultType type, @Assisted("errorMessage") String errorMessage) {
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
