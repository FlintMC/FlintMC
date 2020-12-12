package net.flintmc.framework.inject.internal.method.optimizer;

import net.flintmc.framework.inject.method.MethodInjector;

import java.lang.reflect.Method;

public interface InternalMethodInjector extends MethodInjector {

  void init();

  void setMethod(Method method);

  void setInstance(Object instance);
}
