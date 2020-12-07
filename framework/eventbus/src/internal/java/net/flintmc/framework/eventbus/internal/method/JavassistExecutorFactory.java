package net.flintmc.framework.eventbus.internal.method;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.method.Executor;
import net.flintmc.framework.eventbus.method.ExecutorFactory;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.launcher.LaunchController;
import net.flintmc.transform.javassist.ClassTransformService;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/** An executor factory which uses Javassist to create event executors. */
@Singleton
@Implement(ExecutorFactory.class)
public class JavassistExecutorFactory implements ExecutorFactory {

  private static final int INITIAL_CACHE_CAPACITY = 16;
  private final String session = UUID.randomUUID().toString().replace("-", "");
  private final AtomicInteger identifier = new AtomicInteger();

  private final LoadingCache<CtMethod, Class<? extends Executor<?>>> cache;
  private final Logger logger;
  private final ClassTransformService classTransformService;

  @Inject
  private JavassistExecutorFactory(
      @InjectLogger Logger logger, ClassTransformService classTransformService) {
    this.logger = logger;
    this.classTransformService = classTransformService;

    this.cache =
        CacheBuilder.newBuilder()
            .initialCapacity(INITIAL_CACHE_CAPACITY)
            .weakValues()
            .build(
                new CacheLoader<CtMethod, Class<? extends Executor<?>>>() {
                  @Override
                  public Class<? extends Executor<?>> load(CtMethod targetMethod) throws Exception {
                    Objects.requireNonNull(targetMethod, "targetMethod");

                    return JavassistExecutorFactory.this.generateExecutor(targetMethod);
                  }
                });
  }

  private Class<? extends Executor<?>> generateExecutor(CtMethod targetMethod)
      throws NotFoundException, CannotCompileException, IOException {
    CtClass listener = targetMethod.getDeclaringClass();
    ClassPool cp = listener.getClassPool();
    String className =
        executorClassName(listener, targetMethod, targetMethod.getParameterTypes()[0]);
    CtClass executor = cp.makeClass(className);
    executor.addInterface(cp.get(Executor.class.getName()));

    executor.addField(
        CtField.make(
            String.format(
                "private final %s listener = (%1$s) %s.getInjectedInstance(%1$s.class);",
                listener.getName(), InjectionHolder.class.getName()),
            executor));

    if (Modifier.isPrivate(targetMethod.getModifiers())) {
      targetMethod.setModifiers(javassist.Modifier.setPublic(targetMethod.getModifiers()));
    }

    executor.addMethod(
        CtMethod.make(
            String.format(
                "public void invoke(%s event, %s phase) {"
                    + "((%s) this.listener).%s((%s) event);"
                    + "}",
                Event.class.getName(),
                Subscribe.Phase.class.getName(),
                listener.getName(),
                targetMethod.getName(),
                targetMethod.getParameterTypes()[0].getName()),
            executor));

    return this.defineClass(className, executor.toBytecode());
  }

  @SuppressWarnings("unchecked")
  private Class<? extends Executor<?>> defineClass(String name, byte[] byteCode) {
    return (Class<? extends Executor<?>>)
        LaunchController.getInstance()
            .getRootLoader()
            .commonDefineClass(name, byteCode, 0, byteCode.length, null);
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
        "net.flintmc.framework.eventbus.asm.generated",
        this.session,
        listener.getSimpleName(),
        method.getName(),
        parameter.getSimpleName(),
        this.identifier.incrementAndGet());
  }

  /** {@inheritDoc} */
  @Override
  public Supplier<Executor<?>> create(CtMethod targetMethod) {
    CtClass declaringClass = targetMethod.getDeclaringClass();

    this.classTransformService.addClassTransformation(
        declaringClass,
        context -> {
          try {
            this.transformMethod(context.getCtClass(), targetMethod);
          } catch (NotFoundException exception) {
            this.logger.error(
                String.format(
                    "Failed to generate event listener for %s#%s",
                    declaringClass.getName(), targetMethod.getName()),
                exception);
          }
        });

    return () -> {
      try {
        return cache.getUnchecked(targetMethod).newInstance();
      } catch (InstantiationException | IllegalAccessException exception) {
        this.logger.error(
            String.format(
                "Failed to create ASM executor for %s#%s",
                declaringClass.getName(), targetMethod.getName()),
            exception);
      }
      return null;
    };
  }

  private void transformMethod(CtClass transforming, CtMethod targetMethod)
      throws NotFoundException {
    if (!Modifier.isPublic(transforming.getModifiers())) {
      transforming.setModifiers(
          (transforming.getModifiers() & ~Modifier.PRIVATE & ~Modifier.PROTECTED)
              | Modifier.PUBLIC);
    }

    String[] parameterTypesAsStrings = new String[targetMethod.getParameterTypes().length];
    for (int i = 0; i < parameterTypesAsStrings.length; i++) {
      parameterTypesAsStrings[i] = targetMethod.getParameterTypes()[i].getName();
    }

    CtMethod method =
        transforming.getDeclaredMethod(
            targetMethod.getName(), transforming.getClassPool().get(parameterTypesAsStrings));

    if (!Modifier.isPublic(method.getModifiers())) {
      method.setModifiers(javassist.Modifier.setPublic(method.getModifiers()));
    }
  }
}
