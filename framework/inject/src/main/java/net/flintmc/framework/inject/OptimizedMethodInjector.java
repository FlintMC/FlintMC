package net.flintmc.framework.inject;

import com.google.inject.Key;

import java.util.Map;

public interface OptimizedMethodInjector {

  Object getInstance();

  Object invoke(Map<Key<?>, ?> availableArguments);

  interface ASMFactory {

    OptimizedMethodInjector generate(String targetClass, String methodName);

    OptimizedMethodInjector generate(Object instance, String targetClass, String methodName);
  }
}
