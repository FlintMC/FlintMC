package net.flintmc.framework.eventbus.internal.method;

import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.method.Executor;
import net.flintmc.framework.eventbus.method.SubscribeMethod;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;

import java.util.function.Supplier;

/** A subscribed method in an {@link EventBus}. */
@Implement(SubscribeMethod.class)
public class DefaultSubscribeMethod implements SubscribeMethod {

  private final Class<? extends Event> eventClass;
  private final byte priority;
  private final Subscribe.Phase phase;
  private final Supplier<Executor<?>> executorSupplier;
  private Executor executor;

  @AssistedInject
  private DefaultSubscribeMethod(
      @Assisted Class<? extends Event> eventClass,
      @Assisted byte priority,
      @Assisted Subscribe.Phase phase,
      @Assisted Supplier<Executor<?>> executorSupplier) {
    this.eventClass = eventClass;
    this.priority = priority;
    this.phase = phase;
    this.executorSupplier = executorSupplier;
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
    if (this.executor == null) {
      this.executor = this.executorSupplier.get();
    }
    this.executor.invoke(event, phase);
  }
}
