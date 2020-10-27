package net.flintmc.framework.eventbus.internal.method;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javassist.CtClass;
import javassist.CtMethod;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.method.Executor;
import net.flintmc.framework.eventbus.method.SubscribeMethod;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.stereotype.service.CtResolver;

import java.util.function.Supplier;

/** A subscribed method in an {@link EventBus}. */
@Implement(SubscribeMethod.class)
public class DefaultSubscribeMethod implements SubscribeMethod {

  private final byte priority;
  private final Subscribe.Phase phase;
  private final CtClass ctClass;
  private Executor executor;
  private final Supplier<Executor> executorSupplier;
  private final CtMethod eventMethod;

  private Object instance;

  @AssistedInject
  private DefaultSubscribeMethod(
      @Assisted("priority") byte priority,
      @Assisted("phase") Subscribe.Phase phase,
      @Assisted("declaringClass") CtClass ctClass,
      @Assisted("executorSupplier") Supplier<Executor> executorSupplier,
      @Assisted("eventMethod") CtMethod eventMethod) {
    this.priority = priority;
    this.phase = phase;
    this.ctClass = ctClass;
    this.executorSupplier = executorSupplier;
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
    if (this.executor == null)
      this.executor = this.executorSupplier.get();
    this.executor.invoke(this.instance, event);
  }
}
