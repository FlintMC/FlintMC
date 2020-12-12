package net.flintmc.framework.inject.method;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

public interface MethodInjectionUtils {

  CtMethod generateOptimizedInjector(CtClass target, CtMethod targetMethod)
      throws CannotCompileException;

  CtMethod generateOptimizedInjector(CtClass target, String targetClass, String methodName)
      throws CannotCompileException;
}
