package net.labyfy.component.mappings;

import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.name.Names;
import javassist.CtClass;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.launcher.LaunchController;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Provides method and field mappings for the represented class.
 */
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

  /**
   * Get the java reflect {@link Class} this {@link ClassMapping} is representing from the default {@link net.labyfy.component.launcher.classloading.RootClassLoader}, which Labyfy is using by default.
   *
   * @return The retrieved class
   * @throws ClassNotFoundException if the class could not be found.
   */
  public Class<?> get() throws ClassNotFoundException {
    return get(LaunchController.getInstance().getRootLoader());
  }

  /**
   * Get the java reflect {@link Class} this {@link ClassMapping} is representing from a given {@link ClassLoader}.
   *
   * @param classLoader The class loader to retrieve the class from
   * @return The retrieved class
   * @throws ClassNotFoundException if the class could not be found.
   */
  public Class<?> get(ClassLoader classLoader) throws ClassNotFoundException {
    return Class.forName(this.getName(), false, classLoader);
  }

  /**
   * @return Either {@link ClassMapping#getUnObfuscatedName()} or {@link ClassMapping#getObfuscatedName()} depending on if the current minecraft environment is obfuscated or not.
   */
  public String getName() {
    if (InjectionHolder.getInjectedInstance(Key.get(boolean.class, Names.named("obfuscated")))) {
      return this.obfuscatedName;
    } else {
      return this.unObfuscatedName;
    }
  }

  /**
   * Finds a {@link MethodMapping} in the represented class with a given name and no parameters.
   *
   * @param name the given method name
   * @return MethodMapping found in the represented class
   */
  public MethodMapping getMethod(String name) {
    return this.getMethod(name, new Class[]{});
  }

  /**
   * Finds a {@link MethodMapping} in the represented class with a given name and parameters.
   *
   * @param name       the given method name
   * @param parameters the given method parameter types
   * @return MethodMapping found in the represented class
   */
  public MethodMapping getMethod(String name, Class<?>... parameters) {
    StringBuilder stringBuilder = new StringBuilder();
    for (Class<?> clazz : parameters) {
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

  /**
   * Finds a {@link MethodMapping} in the represented class with a given name and parameters.
   *
   * @param name       the given method name
   * @param parameters the given method parameter types
   * @return MethodMapping found in the represented class
   */
  public MethodMapping getMethod(String name, CtClass... parameters) {
    StringBuilder stringBuilder = new StringBuilder();
    for (CtClass clazz : parameters) {
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

  private String getMethodIdenfitier(CtClass... classes) {
    StringBuilder s = new StringBuilder("(");
    for (final CtClass c : classes) {
      s.append(getDescriptorForClass(c));
    }
    s.append(')');
    return s.toString();
  }

  private String getMethodIdenfitier(Class<?>... classes) {
    StringBuilder s = new StringBuilder("(");
    for (final Class<?> c : classes) {
      s.append(getDescriptorForClass(c));
    }
    s.append(')');
    return s.toString();
  }

  /**
   * Finds a {@link FieldMapping} by either the obfuscated or unobfuscated name in the represented class.
   * Will return a default {@link FieldMapping} if no mappings could be found.
   *
   * @param name Obfuscated or unobfuscated field name to look for
   * @return The found fieldMapping by the name
   * @see FieldMapping#isDefault()
   */
  public FieldMapping getField(String name) {
    if (this.unObfuscatedFieldMappings.containsKey(name)) {
      return this.unObfuscatedFieldMappings.get(name);
    }
    if (this.obfuscatedFieldMappings.containsKey(name)) {
      return this.obfuscatedFieldMappings.get(name);
    }
    return null;
  }

  /**
   * Find a method by a given obfuscated or unobfuscated method identifier(official java format).
   *
   * @param identifier Identifier of the method to discover
   * @return Method discovered by identifier
   * @see MethodMapping#getUnObfuscatedMethodIdentifier()
   * @see MethodMapping#getObfuscatedMethodIdentifier()
   */
  public MethodMapping getMethodExplicit(String identifier) {
    if (this.unObfuscatedMethodMappings.containsKey(identifier)) {
      return this.unObfuscatedMethodMappings.get(identifier);
    }
    if (this.obfuscatedMethodMappings.containsKey(identifier)) {
      return this.obfuscatedMethodMappings.get(identifier);
    }
    return null;
  }

  /**
   * Returns true if obfuscatedName and unObfuscatedName are equals.
   * This can happen if either classes are remapped, that have no obfuscated name provided.
   * <p>
   * for example net.minecraft.client.Main contains obfuscated references but does not have an obfuscated name.
   * This is defined as a "default" class mapping.
   *
   * @return if this class mapping is "default"
   */
  public boolean isDefault() {
    return Objects.equals(this.obfuscatedName, this.unObfuscatedName);
  }

  /**
   * @return All known field mappings for the represented class.
   */
  public Collection<FieldMapping> getFields() {
    return Collections.unmodifiableCollection(this.obfuscatedFieldMappings.values());
  }

  private String getDescriptorForClass(final Class<?> c) {
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

  private String getDescriptorForClass(final CtClass c) {
    if (c.isPrimitive()) {
      if (c == CtClass.byteType) return "B";
      if (c == CtClass.charType) return "C";
      if (c == CtClass.doubleType) return "D";
      if (c == CtClass.floatType) return "F";
      if (c == CtClass.intType) return "I";
      if (c == CtClass.longType) return "J";
      if (c == CtClass.shortType) return "S";
      if (c == CtClass.booleanType) return "Z";
      if (c == CtClass.voidType) return "V";
      throw new RuntimeException("Unrecognized primitive " + c);
    }
    if (c.isArray()) return c.getName().replace('.', '/');
    return ('L' + c.getName() + ';').replace('.', '/');
  }

  /**
   * @return The obfuscated class name.
   */
  public String getObfuscatedName() {
    return obfuscatedName;
  }

  /**
   * @return The unobfuscated class name.
   */
  public String getUnObfuscatedName() {
    return unObfuscatedName;
  }

  /**
   * @return All known method mappings.
   */
  public Collection<MethodMapping> getMethods() {
    return this.obfuscatedMethodMappings.values();
  }

  public static ClassMapping create(
      ClassMappingProvider classMappingProvider, String obfuscatedName, String unObfuscatedName) {
    return new ClassMapping(classMappingProvider, obfuscatedName, unObfuscatedName);
  }

  protected void setMethods(Collection<MethodMapping> methods) {
    this.obfuscatedMethodMappings.clear();
    this.unObfuscatedMethodMappings.clear();

    for (MethodMapping entry : methods) {
      this.obfuscatedMethodMappings.put(entry.getObfuscatedMethodIdentifier(), entry);
      this.unObfuscatedMethodMappings.put(entry.getUnObfuscatedMethodIdentifier(), entry);
    }
  }
}
