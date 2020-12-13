package net.flintmc.framework.eventbus.internal.method;

import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.method.EventExecutor;
import net.flintmc.framework.eventbus.method.SubscribeMethod;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;

/** A subscribed method in an {@link EventBus}. */
@Implement(SubscribeMethod.class)
public class DefaultSubscribeMethod implements SubscribeMethod {

  private final Class<? extends Event> eventClass;
  private final byte priority;
  private final Subscribe.Phase phase;
  private final EventExecutor executor;

  @AssistedInject
  private DefaultSubscribeMethod(
      @Assisted Class<? extends Event> eventClass,
      @Assisted byte priority,
      @Assisted Subscribe.Phase phase,
      @Assisted EventExecutor<?> executor) {
    this.eventClass = eventClass;
    this.priority = priority;
    this.phase = phase;
    this.executor = executor;
  }

  /** {@inheritDoc} */
  @Override
  public Class<? extends Event> getEventClass() {
    return this.eventClass;
  }

  /** {@inheritDoc} */
  @Override
  public byte getPriority() {
    return this.priority;
  }

  /** {@inheritDoc} */
  @Override
  public Subscribe.Phase getPhase() {
    return this.phase;
  }

  /** {@inheritDoc} */
  @Override
  public void invoke(Event event, Subscribe.Phase phase) throws Throwable {
    this.executor.invoke(event, phase, this);
  }
}
