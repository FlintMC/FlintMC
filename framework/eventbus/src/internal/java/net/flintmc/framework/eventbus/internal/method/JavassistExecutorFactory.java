package net.flintmc.framework.eventbus.internal.method;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.eventbus.method.Executor;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.launcher.LaunchController;
import net.flintmc.transform.javassist.ClassTransformService;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/** An executor factory which used ASM to create event executors. */
@Singleton
@Implement(Executor.Factory.class)
public class JavassistExecutorFactory implements Executor.Factory {

  private static final int INITIAL_CACHE_CAPACITY = 16;
  private final String session = UUID.randomUUID().toString().replace("-", "");
  private final AtomicInteger identifier = new AtomicInteger();

  private final LoadingCache<CtMethod, Class<? extends Executor>> cache;
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
  public Supplier<Executor> create(CtClass declaringClass, CtMethod ctMethod)
      throws IllegalAccessException, InstantiationException {

    this.classTransformService.addClassTransformation(
        declaringClass,
        classTransformContext -> {
          CtClass ctClass = classTransformContext.getCtClass();
          if (!Modifier.isPublic(ctClass.getModifiers())) {
            //      throw new ExecutorGenerationException("Listener class must be public.");
            //      /* TODO: allow for modification
            ctClass.setModifiers(
                (ctClass.getModifiers() & ~Modifier.PRIVATE & ~Modifier.PROTECTED)
                    | Modifier.PUBLIC);
            //      */
          }
          try {

            String[] parameterTypesAsStrings = new String[ctMethod.getParameterTypes().length];
            for (int i = 0; i < parameterTypesAsStrings.length; i++) {
              parameterTypesAsStrings[i] = ctMethod.getParameterTypes()[i].getName();
            }

            CtMethod method =
                ctClass.getDeclaredMethod(
                    ctMethod.getName(), ctClass.getClassPool().get(parameterTypesAsStrings));

            if (!Modifier.isPublic(method.getModifiers())) {
              //      throw new ExecutorGenerationException("Listener method must be public.");
              //      /* TODO: allow for modification
              method.setModifiers(
                  (method.getModifiers() & ~Modifier.PRIVATE & ~Modifier.PROTECTED)
                      | Modifier.PUBLIC);
              //      */
            }
          } catch (NotFoundException e) {
            logger.error(e);
          }
        });

    return () -> {
      try {
        return cache.getUnchecked(ctMethod).newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        logger.error(e);
      }
      return null;
    };
  }
}
