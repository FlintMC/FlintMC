package net.flintmc.framework.inject.method;

import javassist.CtMethod;

public interface MethodInjector {

  Object getInstance();

  void setInstance(Object instance);

  interface Factory {

    <T> T generate(CtMethod targetMethod, Class<T> ifc) throws MethodInjectorGenerationException;

    <T> T generate(Object instance, CtMethod targetMethod, Class<T> ifc) throws MethodInjectorGenerationException;
  }
}
