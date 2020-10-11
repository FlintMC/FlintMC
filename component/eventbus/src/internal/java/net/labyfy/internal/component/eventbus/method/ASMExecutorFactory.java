package net.labyfy.internal.component.eventbus.method;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import net.labyfy.component.eventbus.method.Executor;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.launcher.LaunchController;
import net.labyfy.internal.component.eventbus.exception.ExecutorGenerationException;

import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/** An executor factory which used ASM to create event executors. */
@Singleton
@Implement(Executor.Factory.class)
public class ASMExecutorFactory implements Executor.Factory {

  private final String session = UUID.randomUUID().toString().replace("-", "");
  private final AtomicInteger identifier = new AtomicInteger();

  private final LoadingCache<CtMethod, Class<? extends Executor>> cache;

  @Inject
  private ASMExecutorFactory() {
    this.cache =
        CacheBuilder.newBuilder()
            .initialCapacity(16)
            .weakValues()
            .build(
                new CacheLoader<CtMethod, Class<? extends Executor>>() {
                  @Override
                  public Class<? extends Executor> load(CtMethod method) throws Exception {
                    Objects.requireNonNull(method, "method");

                    CtClass listener = method.getDeclaringClass();
                    ClassPool cp = listener.getClassPool();
                    String listenerName =
                        executorClassName(listener, method, method.getParameterTypes()[0]);
                    CtClass executor = cp.makeClass(listenerName);
                    executor.addInterface(cp.get(Executor.class.getName()));
                    executor.addMethod(
                        CtMethod.make(
                            String.format(
                                "public void invoke(Object listener, Object event) {"
                                    + "((%s) listener).%s((%s) event);"
                                    + "}",
                                listener.getName(),
                                method.getName(),
                                method.getParameterTypes()[0].getName()),
                            executor));

                    byte[] byteCode = executor.toBytecode();

                    @SuppressWarnings("unchecked")
                    Class<? extends Executor> executorClass =
                        (Class<? extends Executor>)
                            LaunchController.getInstance()
                                .getRootLoader()
                                .commonDefineClass(
                                    listenerName, byteCode, 0, byteCode.length, null);

                    return executorClass;
                  }
                });
  }

  /**
   * Retrieves the formatted {@link Executor} class name.
   *
   * @param listener The class of the listener.
   * @param method The subscribed method.
   * @param parameter The first parameter of the subscribed method.
   * @return The formatted {@link Executor} class name.
   */
  private String executorClassName(
      final CtClass listener, final CtMethod method, final CtClass parameter) {
    return String.format(
        "%s.%s.%s-%s-%s-%d",
        "net.labyfy.component.event.asm.generated",
        this.session,
        listener.getSimpleName(),
        method.getName(),
        parameter.getSimpleName(),
        this.identifier.incrementAndGet());
  }

  /** {@inheritDoc} */
  @Override
  public Executor create(CtClass declaringClass, CtMethod method)
      throws IllegalAccessException, InstantiationException {
    if (!Modifier.isPublic(declaringClass.getModifiers())) {
      throw new ExecutorGenerationException("Listener class must be public.");
      /* TODO: allow for modification
      declaringClass.setModifiers(
          (declaringClass.getModifiers() & ~Modifier.PRIVATE & ~Modifier.PROTECTED) | Modifier.PUBLIC);*/
    }
    if (!Modifier.isPublic(method.getModifiers())) {
      throw new ExecutorGenerationException("Listener method must be public.");
      /* TODO: allow for modification
      method.setModifiers((method.getModifiers() & ~Modifier.PRIVATE & ~Modifier.PROTECTED) | Modifier.PUBLIC);*/
    }
    return this.cache.getUnchecked(method).newInstance();
  }
}
