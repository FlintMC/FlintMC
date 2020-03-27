package net.labyfy.component.inject.invoke;

import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import net.labyfy.component.inject.InjectionHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class InjectedInvocationHelper {

  public void invokeMethod(Method method) {
    this.invokeMethod(method, Maps.newHashMap());
  }

  public void invokeMethod(Method method, Map<Key<?>, ?> availableArguments) {
    this.invokeMethod(
        method,
        InjectionHolder.getInjectedInstance(method.getDeclaringClass()),
        availableArguments);
  }

  public void invokeMethod(Method method, Object instance) {
    this.invokeMethod(method, instance, Maps.newHashMap());
  }

  public void invokeMethod(Method method, Object instance, Map<Key<?>, ?> availableArguments) {
    try {

      List<Dependency<?>> dependencies =
          InjectionPoint.forMethod(method, TypeLiteral.get(method.getDeclaringClass()))
              .getDependencies();

      Map<Key<?>, Object> invocationArguments = new HashMap<>();

      for (Dependency<?> dependency : dependencies) {
        if (availableArguments.containsKey(dependency.getKey())) {
          invocationArguments.put(dependency.getKey(), availableArguments.get(dependency.getKey()));
        } else {
          invocationArguments.put(
              dependency.getKey(), InjectionHolder.getInjectedInstance(dependency.getKey()));
        }
      }

      Object[] finalArguments = new Object[dependencies.size()];
      for (int i = 0; i < dependencies.size(); i++) {
        finalArguments[i] = invocationArguments.get(dependencies.get(i).getKey());
      }
      method.invoke(instance, finalArguments);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
