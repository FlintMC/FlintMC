package net.labyfy.component.initializer.inject.module;

import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.*;

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

  private final AtomicReference<Injector> injectorReference;

  @Inject
  private PostConstructModule(@Named("injectorReference") AtomicReference injectorReference) {
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
                            .forEach(method -> {
                              try {
                                this.call(injectee, method);
                              } catch (InvocationTargetException exception) {
                                throw new RuntimeException(method.getDeclaringClass().getName() + "#" + method.getName() + " threw an exception", exception);
                              } catch (IllegalAccessException exception) {
                                throw new RuntimeException("unable to access method definition: " + method.getDeclaringClass().getName() + "#" + method.getName() + " threw an exception", exception);
                              }
                            }));
          }

          private void call(Object object, Method method) throws InvocationTargetException, IllegalAccessException {
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
            method.invoke(object, arguments);
          }

          private Collection<Method> getPostConstructMethods(Class<?> clazz) {
            return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(PostConstruct.class))
                .collect(Collectors.toSet());
          }
        });
  }
}
