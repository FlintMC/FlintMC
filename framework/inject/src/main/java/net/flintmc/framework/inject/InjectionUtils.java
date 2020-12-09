package net.flintmc.framework.inject;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;

public interface InjectionUtils {

  String generateInjectedFieldName();

  // field MAY be final

  CtField addInjectedField(
      CtClass declaringClass,
      String fieldName,
      String injectedTypeName,
      boolean multipleInstances,
      boolean staticField)
      throws CannotCompileException;
}
