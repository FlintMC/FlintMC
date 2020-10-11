package net.labyfy.internal.component.eventbus.method;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javassist.CtClass;
import javassist.CtMethod;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.eventbus.method.Executor;
import net.labyfy.component.eventbus.method.SubscribeMethod;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.stereotype.service.CtResolver;

/** A subscribed method in an {@link EventBus}. */
@Implement(SubscribeMethod.class)
public class DefaultSubscribeMethod implements SubscribeMethod {

  private final byte priority;
  private final Subscribe.Phase phase;
  private final CtClass ctClass;
  private final Executor executor;
  private final CtMethod eventMethod;

  private Object instance;

  @AssistedInject
  private DefaultSubscribeMethod(
      @Assisted("priority") byte priority,
      @Assisted("phase") Subscribe.Phase phase,
      @Assisted("declaringClass") CtClass ctClass,
      @Assisted("executor") Executor executor,
      @Assisted("eventMethod") CtMethod eventMethod) {
    this.priority = priority;
    this.phase = phase;
    this.ctClass = ctClass;
    this.executor = executor;
    this.eventMethod = eventMethod;
  }

  /** {@inheritDoc} */
  @Override
  public byte getPriority() {
    return this.priority;
  }

  /** {@inheritDoc} */
  @Override
  public CtMethod getEventMethod() {
    return this.eventMethod;
  }

  /** {@inheritDoc} */
  @Override
  public Subscribe.Phase getPhase() {
    return this.phase;
  }

  /** {@inheritDoc} */
  @Override
  public void invoke(Object event) throws Throwable {
    if (this.instance == null) {
      this.instance = InjectionHolder.getInjectedInstance(CtResolver.get(this.ctClass));
    }
    this.executor.invoke(this.instance, event);
  }
}
