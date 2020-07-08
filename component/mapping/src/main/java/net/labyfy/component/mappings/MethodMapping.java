package net.labyfy.component.mappings;

import com.google.inject.Key;
import com.google.inject.name.Names;
import net.labyfy.component.inject.primitive.InjectionHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class MethodMapping {

  private final ClassMapping classMapping;
  private final String obfuscatedMethodName;
  private final String obfuscatedMethodIdentifier;
  private final String obfuscatedMethodDescription;
  private final String unObfuscatedMethodName;
  private String unObfuscatedMethodIdentifier;
  private String unObfuscatedMethodDescription;
  private Method cached;

  private MethodMapping(
      ClassMapping labyClassMapping,
      String obfuscatedMethodName,
      String obfuscatedMethodIdentifier,
      String obfuscatedMethodDescription,
      String unObfuscatedMethodName,
      String unObfuscatedMethodIdentifier,
      String unObfuscatedMethodDescription) {
    this.classMapping = labyClassMapping;

    this.obfuscatedMethodName = obfuscatedMethodName;
    this.obfuscatedMethodIdentifier = obfuscatedMethodIdentifier;
    this.obfuscatedMethodDescription = obfuscatedMethodDescription;
    this.unObfuscatedMethodName = unObfuscatedMethodName;
    this.unObfuscatedMethodIdentifier = unObfuscatedMethodIdentifier;
    this.unObfuscatedMethodDescription = unObfuscatedMethodDescription;
  }

  /**
   * @return Either {@link MethodMapping#getUnObfuscatedMethodName()} or {@link MethodMapping#getObfuscatedMethodName()} depending on if the current minecraft environment is obfuscated or not.
   */
  public String getName() {
    if (InjectionHolder.getInjectedInstance(Key.get(boolean.class, Names.named("obfuscated")))) {
      return this.obfuscatedMethodName;
    } else {
      return this.unObfuscatedMethodName;
    }
  }

  /**
   * @return return the obfuscated method name this {@link MethodMapping} is representing.
   */
  public String getObfuscatedMethodName() {
    return obfuscatedMethodName;
  }

  /**
   * Format example:
   * {@code
   * public void test(String args){
   * }
   * }
   * <p>
   * will result in test(Ljava/lang/String;)V
   *
   * @return return the obfuscated method identifier this {@link MethodMapping} is representing.
   */
  public String getObfuscatedMethodIdentifier() {
    return obfuscatedMethodIdentifier;
  }

  /**
   * Format example:
   * {@code
   * public void test(String args){
   * }
   * }
   * <p>
   * will result in (Ljava/lang/String;)V
   *
   * @return return the obfuscated method description this {@link MethodMapping} is representing.
   */
  public String getObfuscatedMethodDescription() {
    return obfuscatedMethodDescription;
  }

  /**
   * @return return the unobfuscated method name this {@link MethodMapping} is representing.
   */
  public String getUnObfuscatedMethodName() {
    return unObfuscatedMethodName;
  }

  /**
   * Format example:
   * {@code
   * public void test(String args){
   * }
   * }
   * <p>
   * will result in test(Ljava/lang/String;)V
   *
   * @return return the unobfuscated method identifier this {@link MethodMapping} is representing.
   */
  public String getUnObfuscatedMethodIdentifier() {
    return unObfuscatedMethodIdentifier;
  }

  /**
   * internal use
   */
  public MethodMapping setUnObfuscatedMethodIdentifier(String unObfuscatedMethodIdentifier) {
    this.unObfuscatedMethodIdentifier = unObfuscatedMethodIdentifier;
    this.classMapping.unObfuscatedMethodMappings.put(unObfuscatedMethodIdentifier, this);
    return this;
  }

  public static MethodMapping create(
      ClassMapping labyClassMapping,
      String obfuscatedMethodName,
      String obfuscatedMethodIdentifier,
      String obfuscatedMethodDescription,
      String unObfuscatedMethodName,
      String unObfuscatedMethodIdentifier,
      String unObfuscatedMethodDescription) {
    return new MethodMapping(
        labyClassMapping,
        obfuscatedMethodName,
        obfuscatedMethodIdentifier,
        obfuscatedMethodDescription,
        unObfuscatedMethodName,
        unObfuscatedMethodIdentifier,
        unObfuscatedMethodDescription);
  }

  /**
   * Format example:
   * {@code
   * public void test(String args){
   * }
   * }
   * <p>
   * will result in test(Ljava/lang/String;)V
   *
   * @return return the unobfuscated method identifier this {@link MethodMapping} is representing.
   */
  public String getUnObfuscatedMethodDescription() {
    return unObfuscatedMethodDescription;
  }

  /**
   * internal use
   */
  public MethodMapping setUnObfuscatedMethodDescription(String unObfuscatedMethodDescription) {
    this.unObfuscatedMethodDescription = unObfuscatedMethodDescription;
    return this;
  }

  /**
   * Invoke the method that is represented by this {@link MethodMapping} in a static context.
   *
   * @param parameters The method parameters to call the target method
   * @param <T>        Implicit casted result type
   * @return The return value of the invoked method
   * @throws ClassNotFoundException    If the class could not be found.
   * @throws InvocationTargetException If the method threw an exception.
   * @throws IllegalAccessException    If the method definition could not be accessed.
   */
  public <T> T invokeStatic(Object... parameters) throws IllegalAccessException, InvocationTargetException, ClassNotFoundException {
    return this.invoke(null, parameters);
  }

  /**
   * Invoke the method that is represented by this {@link MethodMapping} in a non static context.
   *
   * @param parameters The method parameters to call the target method
   * @param instance   The instance to invoke the method on
   * @param <T>        Implicit casted result type
   * @return The return value of the invoked method
   * @throws ClassNotFoundException    If the class could not be found.
   * @throws InvocationTargetException If the method threw an exception.
   * @throws IllegalAccessException    If the method definition could not be accessed.
   */
  public <T> T invoke(Object instance, Object... parameters) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
    return (T) this.getMethod().invoke(instance, parameters);
  }

  private String getDescriptorForClass(final Class c) {
    if (c.isPrimitive()) {
      if (c == byte.class) return "B";
      if (c == char.class) return "C";
      if (c == double.class) return "D";
      if (c == float.class) return "F";
      if (c == int.class) return "I";
      if (c == long.class) return "J";
      if (c == short.class) return "S";
      if (c == boolean.class) return "Z";
      if (c == void.class) return "V";
      throw new RuntimeException("Unrecognized primitive " + c);
    }
    if (c.isArray()) return c.getName().replace('.', '/');
    return ('L' + c.getName() + ';').replace('.', '/');
  }

  /**
   * @return the parent class mapping
   */
  public ClassMapping getClassMapping() {
    return this.classMapping;
  }

  /**
   * @return The java reflect {@link Method} this {@link MethodMapping} represents.
   * @throws ClassNotFoundException If the class could not be found.
   */
  public Method getMethod() throws ClassNotFoundException {
    if (this.cached == null) {
      if (this.unObfuscatedMethodName.equals(this.obfuscatedMethodName)
          && this.unObfuscatedMethodDescription.equals(this.obfuscatedMethodDescription)) {
        for (Method declaredMethod : this.classMapping.get().getDeclaredMethods()) {
          if (declaredMethod.getName().equals(this.obfuscatedMethodName)
              && this.getMethodDescriptor(declaredMethod)
              .startsWith(this.obfuscatedMethodDescription)) {
            declaredMethod.setAccessible(true);
            this.cached = declaredMethod;
            break;
          }
        }
      }

      if (InjectionHolder.getInjectedInstance(Key.get(boolean.class, Names.named("obfuscated")))) {
        for (Method declaredMethod : this.classMapping.get().getDeclaredMethods()) {
          if (declaredMethod.getName().equals(this.obfuscatedMethodName)
              && this.getMethodDescriptor(declaredMethod)
              .equals(this.obfuscatedMethodDescription)) {
            declaredMethod.setAccessible(true);
            this.cached = declaredMethod;
            break;
          }
        }
      } else {
        for (Method declaredMethod : this.classMapping.get().getDeclaredMethods()) {
          if (declaredMethod.getName().equals(this.unObfuscatedMethodName)
              && this.getMethodDescriptor(declaredMethod)
              .equals(this.unObfuscatedMethodDescription)) {
            declaredMethod.setAccessible(true);
            this.cached = declaredMethod;
            break;
          }
        }
      }
    }
    return this.cached;
  }

  /**
   * Returns true if obfuscatedName and unObfuscatedName are equals.
   *
   * @return if this class mapping is "default"
   */
  public boolean isDefault() {
    return Objects.equals(this.obfuscatedMethodName, this.unObfuscatedMethodName) && Objects.equals(this.obfuscatedMethodIdentifier, this.unObfuscatedMethodIdentifier);
  }

  private String getMethodDescriptor(Method m) {
    StringBuilder s = new StringBuilder("(");
    for (final Class c : m.getParameterTypes()) {
      s.append(getDescriptorForClass(c));
    }
    s.append(')');
    return s + getDescriptorForClass(m.getReturnType());
  }
}
