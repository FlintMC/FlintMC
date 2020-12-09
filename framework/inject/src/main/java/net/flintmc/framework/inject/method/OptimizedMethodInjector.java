package net.flintmc.framework.inject.method;

import com.google.inject.Key;

import java.util.Map;

public interface OptimizedMethodInjector {

  Object getInstance();

  Object invoke(Map<Key<?>, ?> availableArguments);

  interface Factory {

    OptimizedMethodInjector generate(String targetClass, String methodName);

    OptimizedMethodInjector generate(Object instance, String targetClass, String methodName);
  }
}
