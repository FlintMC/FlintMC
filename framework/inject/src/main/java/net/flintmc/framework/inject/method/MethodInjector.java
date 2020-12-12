package net.flintmc.framework.inject.method;

import com.google.inject.Key;
import javassist.CtMethod;

import java.util.Map;

public interface MethodInjector {

  Object getInstance();

  Object invoke(Map<Key<?>, ?> availableArguments);

  interface Factory {

    default MethodInjector generate(CtMethod method) {
      return this.generate(method.getDeclaringClass().getName(), method.getName());
    }

    default MethodInjector generate(Object instance, CtMethod method) {
      return this.generate(instance, method.getDeclaringClass().getName(), method.getName());
    }

    MethodInjector generate(String targetClass, String methodName);

    MethodInjector generate(Object instance, String targetClass, String methodName);
  }
}
