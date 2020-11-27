package net.flintmc.framework.eventbus.internal.method;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.method.SubscribeMethod;
import net.flintmc.framework.eventbus.method.SubscribeMethodBuilder;
import net.flintmc.framework.inject.implement.Implement;

@Singleton
@Implement(SubscribeMethodBuilder.Factory.class)
public class DefaultSubscribeMethodBuilderFactory implements SubscribeMethodBuilder.Factory {

  private final EventBus eventBus;
  private final SubscribeMethod.Factory methodFactory;

  @Inject
  private DefaultSubscribeMethodBuilderFactory(EventBus eventBus, SubscribeMethod.Factory methodFactory) {
    this.eventBus = eventBus;
    this.methodFactory = methodFactory;
  }

  /** {@inheritDoc} */
  @Override
  public <E extends Event> SubscribeMethodBuilder<E> newBuilder(Class<E> eventClass) {
    return new DefaultSubscribeMethodBuilder<>(this.methodFactory, this.eventBus, eventClass);
  }
}
