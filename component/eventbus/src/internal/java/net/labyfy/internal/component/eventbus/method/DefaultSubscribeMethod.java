package net.labyfy.internal.component.eventbus.method;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.Subscribe;
import net.labyfy.component.eventbus.method.Executor;
import net.labyfy.component.eventbus.method.SubscribeMethod;
import net.labyfy.component.inject.implement.Implement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * A subscribed method in an {@link EventBus}.
 */
@Implement(SubscribeMethod.class)
public class DefaultSubscribeMethod implements SubscribeMethod {

  private final byte priority;
  private final Subscribe.Phase phase;
  private final Object instance;
  private final Executor executor;
  private final Method eventMethod;
  private final Annotation groupAnnotation;

  @AssistedInject
  private DefaultSubscribeMethod(
          @Assisted("priority") byte priority,
          @Assisted("phase") Subscribe.Phase phase,
          @Assisted("instance") Object instance,
          @Assisted("executor") Executor executor,
          @Assisted("eventMethod") Method eventMethod,
          @Assisted("groupAnnotation") Annotation groupAnnotation
  ) {
    this.priority = priority;
    this.phase = phase;
    this.instance = instance;
    this.executor = executor;
    this.eventMethod = eventMethod;
    this.groupAnnotation = groupAnnotation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte getPriority() {
    return this.priority;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Method getEventMethod() {
    return this.eventMethod;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Subscribe.Phase getPhase() {
    return this.phase;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Annotation getGroupAnnotation() {
    return this.groupAnnotation;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void invoke(Object event) throws Throwable {
    this.executor.invoke(this.instance, event);
  }

}
