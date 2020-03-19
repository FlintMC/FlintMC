package net.labyfy.component.mappings;

import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.name.Names;
import net.labyfy.component.inject.InjectionHolder;
import net.minecraft.launchwrapper.Launch;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class ClassMapping {

  private final ClassMappingProvider classMappingProvider;

  protected final Map<String, MethodMapping> obfuscatedMethodMappings = Maps.newConcurrentMap();
  protected final Map<String, MethodMapping> unObfuscatedMethodMappings = Maps.newConcurrentMap();
  private final Map<String, FieldMapping> obfuscatedFieldMappings = Maps.newConcurrentMap();
  private final Map<String, FieldMapping> unObfuscatedFieldMappings = Maps.newConcurrentMap();
  private final String obfuscatedName;
  private final String unObfuscatedName;

  private ClassMapping(
      ClassMappingProvider classMappingProvider, String obfuscatedName, String unObfuscatedName) {
    this.classMappingProvider = classMappingProvider;
    this.obfuscatedName = obfuscatedName;
    this.unObfuscatedName = unObfuscatedName;
  }

  void addMethod(
      String obfuscatedMethodName,
      String obfuscatedMethodIdentifier,
      String obfuscatedMethodDescription,
      String unObfuscatedMethodName,
      String unObfuscatedMethodIdentifier,
      String unObfuscatedMethodDescription) {

    MethodMapping methodMapping =
        MethodMapping.create(
            this,
            obfuscatedMethodName,
            obfuscatedMethodIdentifier,
            obfuscatedMethodDescription,
            unObfuscatedMethodName,
            unObfuscatedMethodIdentifier,
            unObfuscatedMethodDescription);
    if (obfuscatedMethodIdentifier != null)
      this.obfuscatedMethodMappings.put(obfuscatedMethodIdentifier, methodMapping);
    if (unObfuscatedMethodIdentifier != null)
      this.unObfuscatedMethodMappings.put(unObfuscatedMethodIdentifier, methodMapping);
  }

  void addField(String obfuscatedFieldName, String unObfuscatedFieldName) {
    FieldMapping fieldMapping =
        FieldMapping.create(this, obfuscatedFieldName, unObfuscatedFieldName);
    this.obfuscatedFieldMappings.put(obfuscatedFieldName, fieldMapping);
    this.unObfuscatedFieldMappings.put(unObfuscatedFieldName, fieldMapping);
  }

  public Class get() {
    return get(Launch.classLoader);
  }

  public Class get(ClassLoader classLoader) {
    try {
      return Class.forName(this.getName(), false, classLoader);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getName() {
    if (InjectionHolder.getInjectedInstance(Key.get(boolean.class, Names.named("obfuscated")))) {
      return this.obfuscatedName;
    } else {
      return this.unObfuscatedName;
    }
  }

  public MethodMapping getMethod(String name, Class... parameters) {
    StringBuilder stringBuilder = new StringBuilder();
    for (Class clazz : parameters) {
      stringBuilder.append(this.getDescriptorForClass(clazz));
    }

    MethodMapping method =
        this.getMethodExplicit(
            name + this.translateMethodDescription("(" + stringBuilder.toString() + ")"));
    if (method != null) return method;

    String methodIdenfitier = this.getMethodIdenfitier(parameters);
    return MethodMapping.create(
        this, name, methodIdenfitier, methodIdenfitier, name, methodIdenfitier, methodIdenfitier);
  }

  public String translateMethodDescription(String obfDesc) {
    String tempObfDesc = obfDesc.substring(1, obfDesc.lastIndexOf(')'));
    StringBuilder unObfDesc = new StringBuilder("(");
    int currentIndex = 0;

    while (currentIndex != tempObfDesc.length()) {
      if (tempObfDesc.charAt(currentIndex) != 'L') {
        unObfDesc.append(tempObfDesc.charAt(currentIndex++));
      } else {
        int maxIndex = tempObfDesc.substring(currentIndex + 1).indexOf(';') + currentIndex + 1;
        String obfClassName = tempObfDesc.substring(currentIndex + 1, maxIndex);
        ClassMapping descClassMapping = this.classMappingProvider.get(obfClassName);
        unObfDesc
            .append("L")
            .append(
                descClassMapping != null
                    ? descClassMapping.getUnObfuscatedName().replace('.', '/')
                    : obfClassName)
            .append(";");
        currentIndex = maxIndex + 1;
      }
    }

    unObfDesc.append(")");
    return unObfDesc.toString();
  }

  private String getMethodIdenfitier(Class... classes) {
    StringBuilder s = new StringBuilder("(");
    for (final Class c : classes) {
      s.append(getDescriptorForClass(c));
    }
    s.append(')');
    return s.toString();
  }

  public FieldMapping getField(String name) {
    if (this.unObfuscatedFieldMappings.containsKey(name)) {
      return this.unObfuscatedFieldMappings.get(name);
    }
    if (this.obfuscatedFieldMappings.containsKey(name)) {
      return this.obfuscatedFieldMappings.get(name);
    }
    return null;
  }

  public MethodMapping getMethodExplicit(String identifier) {
    if (this.unObfuscatedMethodMappings.containsKey(identifier)) {
      return this.unObfuscatedMethodMappings.get(identifier);
    }
    if (this.obfuscatedMethodMappings.containsKey(identifier)) {
      return this.obfuscatedMethodMappings.get(identifier);
    }
    return null;
  }

  public boolean isDefault() {
    return Objects.equals(this.obfuscatedName, this.unObfuscatedName);
  }

  public Collection<FieldMapping> getFields() {
    return Collections.unmodifiableCollection(this.obfuscatedFieldMappings.values());
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

  public String getObfuscatedName() {
    return obfuscatedName;
  }

  public String getUnObfuscatedName() {
    return unObfuscatedName;
  }

  public Collection<MethodMapping> getMethods() {
    return this.obfuscatedMethodMappings.values();
  }

  public static ClassMapping create(
      ClassMappingProvider classMappingProvider, String obfuscatedName, String unObfuscatedName) {
    return new ClassMapping(classMappingProvider, obfuscatedName, unObfuscatedName);
  }
}
