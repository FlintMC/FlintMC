package net.flintmc.mcapi.internal.world.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.event.WorldLoadEvent;

/** {@inheritDoc} */
@Implement(WorldLoadEvent.class)
public class DefaultWorldLoadEvent implements WorldLoadEvent {

  private final String worldName;
  private State state;
  private float processPercentage;

  @AssistedInject
  public DefaultWorldLoadEvent(
      @Assisted String worldName, @Assisted State state, @Assisted float processPercentage) {
    this.worldName = worldName;
    this.state = state;
    this.processPercentage = processPercentage;
  }

  /** {@inheritDoc} */
  @Override
  public String getWorldName() {
    return this.worldName;
  }

  /** {@inheritDoc} */
  @Override
  public State getState() {
    return this.state;
  }

  public void setType(State state) {
    this.state = state;
  }

  /** {@inheritDoc} */
  @Override
  public float getProcessPercentage() {
    return this.processPercentage;
  }

  public void setProcessPercentage(float processPercentage) {
    this.processPercentage = processPercentage;
  }
}
