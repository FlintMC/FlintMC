package net.flintmc.framework.inject.method;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * Utils for injecting a {@link MethodInjector} into generated code.
 *
 * @see MethodInjector
 */
public interface MethodInjectionUtils {

  /**
   * Generates a new {@link MethodInjector} and a getter for it. The instance of the {@link
   * MethodInjector} will be created when the getter is invoked. The getter may not be in the given
   * {@code target} class, so this code should be used to invoke the getter:
   *
   * <pre>getter.getDeclaringClass().getName() + "." + getter.getName()</pre>
   *
   * <br>
   * The return type of the getter will be the given interface.
   *
   * @param target The target where the getter should be declared (but may not if it is an
   *     interface) or {@code null} to create an extra class to declare the getter in
   * @param targetMethod The non-null method to be invoked by the generated {@link MethodInjector}
   * @param ifc The non-null interface to be implemented
   * @return The new non-null getter which returns the new {@link MethodInjector}
   * @throws CannotCompileException If a compiler error occurred when compiling the generated code
   * @see MethodInjector
   */
  CtMethod generateInjector(CtClass target, CtMethod targetMethod, Class<?> ifc)
      throws CannotCompileException;

  /**
   * Generates a new {@link MethodInjector} and a getter for it. The instance of the {@link
   * MethodInjector} will be created when the getter is invoked. The getter may not be in the given
   * {@code target} class, so this code should be used to invoke the getter:
   *
   * <pre>getter.getDeclaringClass().getName() + "." + getter.getName()</pre>
   *
   * <br>
   * The return type of the getter will be the given interface.
   *
   * @param target The target where the getter should be declared (but may not if it is an
   *     interface) or {@code null} to create an extra class to declare the getter in
   * @param targetClass The non-null name of the class where the method to be invoked by the
   *     generated {@link MethodInjector} is located
   * @param methodName The non-null name of the method to be invoked by the generated {@link
   *     MethodInjector}
   * @param ifc The non-null interface to be implemented
   * @return The new non-null getter which returns the new {@link MethodInjector}
   * @throws CannotCompileException If a compiler error occurred when compiling the generated code
   * @see MethodInjector
   */
  CtMethod generateInjector(CtClass target, String targetClass, String methodName, Class<?> ifc)
      throws CannotCompileException;
}
