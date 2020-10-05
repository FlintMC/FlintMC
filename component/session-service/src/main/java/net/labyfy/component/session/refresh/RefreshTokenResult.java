package net.labyfy.component.session.refresh;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

public interface RefreshTokenResult {

  boolean wasSuccess();

  String getErrorMessage();

  @AssistedFactory(RefreshTokenResult.class)
  interface Factory {

    RefreshTokenResult create(@Assisted("success") boolean success, @Assisted("errorMessage") String errorMessage);

    RefreshTokenResult create(@Assisted("success") boolean success);

  }

}
