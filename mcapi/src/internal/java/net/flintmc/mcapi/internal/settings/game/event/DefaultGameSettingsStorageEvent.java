package net.flintmc.mcapi.internal.settings.game.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.settings.game.event.GameSettingsStorageEvent;

/** Default implementation of the {@link GameSettingsStorageEvent}. */
@Implement(GameSettingsStorageEvent.class)
public class DefaultGameSettingsStorageEvent implements GameSettingsStorageEvent {

  private final State state;

  @AssistedInject
  private DefaultGameSettingsStorageEvent(@Assisted("state") State state) {
    this.state = state;
  }

  /** {@inheritDoc} */
  @Override
  public State getState() {
    return this.state;
  }
}
