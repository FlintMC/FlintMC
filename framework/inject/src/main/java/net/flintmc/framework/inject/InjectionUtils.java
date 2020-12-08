package net.flintmc.framework.inject;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;

public interface InjectionUtils {

  // field will be final

  CtField addInjectedField(CtClass declaringClass, String injectedTypeName)
      throws CannotCompileException;

  CtField addInjectedField(CtClass declaringClass, Class<?> injectedType)
      throws CannotCompileException;

  CtField addInjectedField(CtClass declaringClass, CtClass injectedType)
      throws CannotCompileException;

  CtField addInjectedField(CtClass declaringClass, String fieldName, String injectedTypeName)
      throws CannotCompileException;

  CtField addInjectedField(CtClass declaringClass, String fieldName, Class<?> injectedType)
      throws CannotCompileException;

  CtField addInjectedField(CtClass declaringClass, String fieldName, CtClass injectedType)
      throws CannotCompileException;
}
