package net.labyfy.component.transform.tweaker.mapping;

import java.lang.reflect.Method;

public class MethodMapping  {

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

  public String getObfuscatedMethodName() {
    return obfuscatedMethodName;
  }

  public String getObfuscatedMethodIdentifier() {
    return obfuscatedMethodIdentifier;
  }

  public String getObfuscatedMethodDescription() {
    return obfuscatedMethodDescription;
  }

  public String getUnObfuscatedMethodName() {
    return unObfuscatedMethodName;
  }

  public String getUnObfuscatedMethodIdentifier() {
    return unObfuscatedMethodIdentifier;
  }

  public String getUnObfuscatedMethodDescription() {
    return unObfuscatedMethodDescription;
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

  public ClassMapping getClassMapping() {
    return this.classMapping;
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

  public MethodMapping setUnObfuscatedMethodDescription(String unObfuscatedMethodDescription) {
    this.unObfuscatedMethodDescription = unObfuscatedMethodDescription;
    return this;
  }

  public MethodMapping setUnObfuscatedMethodIdentifier(String unObfuscatedMethodIdentifier) {
    this.unObfuscatedMethodIdentifier = unObfuscatedMethodIdentifier;
    this.classMapping.unObfuscatedMethodMappings.put(unObfuscatedMethodIdentifier, this);
    return this;
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
