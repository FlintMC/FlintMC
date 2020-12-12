package net.flintmc.framework.inject.method;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

public interface MethodInjectionUtils {

  // getter may not be in the same class as target

  CtMethod generateInjector(CtClass target, CtMethod targetMethod, Class<?> ifc)
      throws CannotCompileException;

  CtMethod generateInjector(CtClass target, String targetClass, String methodName, Class<?> ifc)
      throws CannotCompileException;
}
