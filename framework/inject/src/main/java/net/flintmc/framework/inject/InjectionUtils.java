package net.flintmc.framework.inject;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;

/**
 * Utils for injecting instances from the Injector into generated code.
 *
 * @see InjectedFieldBuilder
 */
public interface InjectionUtils {

  /**
   * Generates a new unique field name for a field that should get its value from the Injector.
   *
   * @return The new non-null unique field name
   */
  String generateInjectedFieldName();

  /**
   * Adds a new field to the given {@code declaringClass} with the type {@code injectedTypeName} and
   * the value for this type from the Injector. It may be final and therefore its value should never
   * be changed.
   *
   * @param declaringClass The non-null class where the new field should be declared. Must not be an
   *     interface
   * @param fieldName The non-null name of the field, may not match the name of the result if {@code
   *     singletonInstance} is {@code false}
   * @param injectedTypeName The non-null name of the class that should be injected
   * @param singletonInstance {@code true} if the {@code injectedTypeName} should only injected once
   *     even if this method is called multiple times, {@code false} if a new field should be
   *     created whenever this method is called and even if there is already a field with the given
   *     {@code injectedTypeName}
   * @param staticField {@code true} if the field should be static, {@code false} otherwise
   * @return The new non-null generated field
   * @throws CannotCompileException If a compiler error occurred, should only happen if {@code
   *     fieldName} or {@code singletonInstance} cannot be compiled
   * @see InjectedFieldBuilder
   */
  CtField addInjectedField(
      CtClass declaringClass,
      String fieldName,
      String injectedTypeName,
      boolean singletonInstance,
      boolean staticField)
      throws CannotCompileException;
}
