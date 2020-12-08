package net.flintmc.framework.inject.internal.optimizer;

import net.flintmc.framework.inject.OptimizedMethodInjector;

import java.lang.reflect.Method;

public interface InternalOptimizedMethodInjector extends OptimizedMethodInjector {

  void init(Method method);

  void setInstance(Object instance);
}
