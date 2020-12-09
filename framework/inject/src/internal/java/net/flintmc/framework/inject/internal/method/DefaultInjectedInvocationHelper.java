package net.flintmc.framework.inject.internal.method;

import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import net.flintmc.framework.inject.method.InjectedInvocationHelper;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Default implementation of the {@link InjectedInvocationHelper}. */
@Singleton
@Implement(InjectedInvocationHelper.class)
public class DefaultInjectedInvocationHelper implements InjectedInvocationHelper {

  /** {@inheritDoc} */
  @Override
  public <T> T invokeMethod(Method method)
      throws InvocationTargetException, IllegalAccessException {
    return this.invokeMethod(method, Maps.newHashMap());
  }

  /** {@inheritDoc} */
  @Override
  public <T> T invokeMethod(Method method, Map<Key<?>, ?> availableArguments)
      throws InvocationTargetException, IllegalAccessException {
    return this.invokeMethod(
        method,
        InjectionHolder.getInjectedInstance(method.getDeclaringClass()),
        availableArguments);
  }

  /** {@inheritDoc} */
  @Override
  public <T> T invokeMethod(Method method, Object instance)
      throws InvocationTargetException, IllegalAccessException {
    return this.invokeMethod(method, instance, Maps.newHashMap());
  }

  /** {@inheritDoc} */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T invokeMethod(Method method, Object instance, Map<Key<?>, ?> availableArguments)
      throws InvocationTargetException, IllegalAccessException {
    // Retrieves all dependencies required for injecting
    List<Dependency<?>> dependencies =
        InjectionPoint.forMethod(method, TypeLiteral.get(method.getDeclaringClass()))
            .getDependencies();

    Map<Key<?>, Object> invocationArguments = new HashMap<>();

    for (Dependency<?> dependency : dependencies) {
      if (availableArguments.containsKey(dependency.getKey())) {
        // If the arguments already contain an available injection, use it
        invocationArguments.put(dependency.getKey(), availableArguments.get(dependency.getKey()));
      } else {
        // Else retrieve an instance from Guice
        invocationArguments.put(
            dependency.getKey(), InjectionHolder.getInjectedInstance(dependency.getKey()));
      }
    }

    // Convert the arguments so they can be used with reflection
    Object[] finalArguments = new Object[dependencies.size()];
    for (int i = 0; i < dependencies.size(); i++) {
      finalArguments[i] = invocationArguments.get(dependencies.get(i).getKey());
    }

    // Invoke the method
    return (T) method.invoke(instance, finalArguments);
  }
}
