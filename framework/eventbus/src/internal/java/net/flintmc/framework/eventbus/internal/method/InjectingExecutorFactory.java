package net.flintmc.framework.eventbus.internal.method;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javassist.CtMethod;
import net.flintmc.framework.eventbus.method.EventExecutor;
import net.flintmc.framework.eventbus.method.ExecutorFactory;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.method.MethodInjector;
import net.flintmc.framework.stereotype.service.CtResolver;

/** An executor factory which uses the {@link MethodInjector} to create event executors. */
@Singleton
@Implement(ExecutorFactory.class)
public class InjectingExecutorFactory implements ExecutorFactory {

  private final MethodInjector.Factory injectorFactory;
  private final Map<Integer, EventExecutor<?>> executors;

  @Inject
  private InjectingExecutorFactory(MethodInjector.Factory injectorFactory) {
    this.injectorFactory = injectorFactory;
    this.executors = new ConcurrentHashMap<>();
  }

  /** {@inheritDoc} */
  @Override
  public EventExecutor<?> create(CtMethod targetMethod) {
    int hash = CtResolver.hash(targetMethod);
    if (this.executors.containsKey(hash)) {
      return this.executors.get(hash);
    }

    // we need another class because Guice cannot read the dependencies for generic parameter types
    GeneratedEventExecutor executor =
        this.injectorFactory.generate(targetMethod, GeneratedEventExecutor.class);
    EventExecutor<?> eventExecutor = executor::invoke;

    this.executors.put(hash, eventExecutor);

    return eventExecutor;
  }
}
