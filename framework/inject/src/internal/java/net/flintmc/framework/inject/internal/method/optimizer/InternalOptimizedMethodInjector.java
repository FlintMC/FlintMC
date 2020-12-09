package net.flintmc.framework.inject.internal.method.optimizer;

import net.flintmc.framework.inject.method.OptimizedMethodInjector;

import java.lang.reflect.Method;

public interface InternalOptimizedMethodInjector extends OptimizedMethodInjector {

  void init();

  void setMethod(Method method);

  void setInstance(Object instance);
}
