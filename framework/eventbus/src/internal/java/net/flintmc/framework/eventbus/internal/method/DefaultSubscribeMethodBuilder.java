package net.flintmc.framework.eventbus.internal.method;

import com.google.common.base.Preconditions;
import java.util.function.Consumer;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.EventPriority;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.method.EventExecutor;
import net.flintmc.framework.eventbus.method.SubscribeMethod;
import net.flintmc.framework.eventbus.method.SubscribeMethodBuilder;

/** {@inheritDoc} */
public class DefaultSubscribeMethodBuilder<E extends Event> implements SubscribeMethodBuilder<E> {

  private final SubscribeMethod.Factory methodFactory;
  private final EventBus eventBus;

  private final Class<E> eventClass;
  private byte priority;
  private Subscribe.Phase phase;
  private EventExecutor<?> executor;

  protected DefaultSubscribeMethodBuilder(
      SubscribeMethod.Factory methodFactory, EventBus eventBus, Class<E> eventClass) {
    this.methodFactory = methodFactory;
    this.eventBus = eventBus;
    this.eventClass = eventClass;

    this.priority = EventPriority.NORMAL;
    this.phase = Subscribe.Phase.ANY;
  }

  /** {@inheritDoc} */
  @Override
  public SubscribeMethodBuilder<E> phaseOnly(Subscribe.Phase phase) {
    this.phase = phase;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public SubscribeMethodBuilder<E> priority(byte priority) {
    this.priority = priority;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public SubscribeMethodBuilder<E> to(Consumer<E> executor) {
    return this.to((event, phase, holderMethod) -> executor.accept(event));
  }

  /** {@inheritDoc} */
  @Override
  public SubscribeMethodBuilder<E> to(EventExecutor<E> executor) {
    this.executor = executor;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public SubscribeMethod build() throws NullPointerException {
    Preconditions.checkNotNull(this.executor, "to() has not been called");

    return this.methodFactory.create(this.eventClass, this.priority, this.phase, this.executor);
  }

  /** {@inheritDoc} */
  @Override
  public void buildAndRegister() throws NullPointerException {
    SubscribeMethod method = this.build();
    this.eventBus.registerSubscribeMethod(method);
  }
}
