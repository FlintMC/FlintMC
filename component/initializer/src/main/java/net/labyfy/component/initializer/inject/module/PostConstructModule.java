package net.labyfy.component.initializer.inject.module;

import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.*;
import net.labyfy.component.inject.logging.InjectLogger;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Enables the {@link PostConstruct} annotation.
 * Whenever an object is constructed via Dependency Injection, all methods in this object, annotated with {@link PostConstruct}
 * will be called.
 */
@Singleton
public class PostConstructModule extends AbstractModule {

  private final Logger logger;
  private final AtomicReference<Injector> injectorReference;

  @Inject
  private PostConstructModule(
      @InjectLogger Logger logger,
      @Named("injectorReference") AtomicReference injectorReference
  ) {
    this.logger = logger;
    this.injectorReference = injectorReference;
  }

  @Override
  protected void configure() {
    this.bindListener(
        Matchers.any(),
        new TypeListener() {
          public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
            encounter.register(
                (InjectionListener<I>)
                    injectee ->
                        this.getPostConstructMethods(injectee.getClass())
                            .forEach(method -> this.call(injectee, method)));
          }

          private void call(Object object, Method method) {
            Map<? extends Class<?>, ?> dependencies =
                InjectionPoint.forMethod(method, TypeLiteral.get(method.getDeclaringClass()))
                    .getDependencies().stream()
                    .map(Dependency::getKey)
                    .map(Key::getTypeLiteral)
                    .map(TypeLiteral::getRawType)
                    .collect(
                        Collectors.toMap(
                            type -> type, type -> injectorReference.get().getInstance(type)));

            Object[] arguments = new Object[method.getParameterTypes().length];
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
              arguments[i] = dependencies.get(parameterTypes[i]);
            }
            method.setAccessible(true);
            try {
              method.invoke(object, arguments);
            } catch (IllegalAccessException | InvocationTargetException exception) {
              logger.error("unable to invoke method: {}#{}", object.getClass().getName(), method.getName());
              logger.error(exception);
            }
          }

          private Collection<Method> getPostConstructMethods(Class<?> clazz) {
            return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(PostConstruct.class))
                .collect(Collectors.toSet());
          }
        });
  }
}
