package net.flintmc.mcapi.internal.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.FieldOfViewModifier;
import net.flintmc.mcapi.player.event.FieldOfViewEvent;
import net.flintmc.mcapi.player.event.FieldOfViewEvent.Factory;

/**
 * Default implementation of the {@link FieldOfViewEvent}.
 */
@Singleton
@Implement(FieldOfViewModifier.class)
public class DefaultFieldOfViewModifier implements FieldOfViewModifier {

  private final EventBus eventBus;
  private final FieldOfViewEvent.Factory fieldOfViewEventFactory;

  private FieldOfViewEvent fieldOfViewEvent;

  @Inject
  private DefaultFieldOfViewModifier(EventBus eventBus, Factory fieldOfViewEventFactory) {
    this.eventBus = eventBus;
    this.fieldOfViewEventFactory = fieldOfViewEventFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float fieldOfView(float fov) {
    if (this.fieldOfViewEvent == null) {
      this.fieldOfViewEvent = this.fieldOfViewEventFactory.create(fov);
    } else {
      this.fieldOfViewEvent.setFov(fov);
    }

    this.eventBus.fireEvent(this.fieldOfViewEvent, Phase.PRE);

    // other logic

    this.eventBus.fireEvent(this.fieldOfViewEvent, Phase.POST);

    return this.fieldOfViewEvent.getFov();
  }
}
