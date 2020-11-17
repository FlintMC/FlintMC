package net.flintmc.mcapi.internal.player.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.event.FieldOfViewEvent;

@Implement(FieldOfViewEvent.class)
public class DefaultFieldOfViewEvent implements FieldOfViewEvent {

  private float fov;

  @AssistedInject
  public DefaultFieldOfViewEvent(@Assisted("fov") float fov) {
    this.fov = fov;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getFov() {
    return this.fov;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFov(float fov) {
    this.fov = fov;
  }
}
